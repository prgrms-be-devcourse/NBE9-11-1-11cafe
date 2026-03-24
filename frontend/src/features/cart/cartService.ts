import type { CartItem } from './cartTypes'
import { productImageSrcByName } from './cartProductImages'

// NOTE: 지금은 백엔드 대신 메모리 기반 목 데이터로 동작합니다.
let cart: CartItem[] = [
  {
    id: '1',
    productName: 'Columbia',
    productType: 'SINGLE_ORIGIN',
    price: 15000,
    quantity: 2,
    imageSrc: productImageSrcByName['Columbia'],
  },
  {
    id: '2',
    productName: 'Ethiopia',
    productType: 'SINGLE_ORIGIN',
    price: 17000,
    quantity: 1,
    imageSrc: productImageSrcByName['Ethiopia'],
  },
  {
    id: '3',
    productName: 'Brazil',
    productType: 'BLENDED',
    price: 13000,
    quantity: 1,
    imageSrc: productImageSrcByName['Brazil'],
  },
  {
    id: '4',
    productName: 'Kenya',
    productType: 'SINGLE_ORIGIN',
    price: 16000,
    quantity: 2,
    imageSrc: productImageSrcByName['Kenya'],
  },
]

function cloneItems(items: CartItem[]): CartItem[] {
  return items.map((item) => ({ ...item }))
}

function delay(ms: number) {
  return new Promise<void>((resolve) => setTimeout(resolve, ms))
}

type CartItemResponse = {
  cartItemId: number
  productId: number
  productName: string
  price: number
  quantity: number
  itemTotal: number
}

type CartResponse = {
  success: boolean
  message: string
  data: {
    cartId: number
    guestId: string
    cartItems: CartItemResponse[]
    totalAmount: number
  }
}

function resolveGuestId() {
  const envGuestId = String(import.meta.env.VITE_GUEST_ID ?? '').trim()
  if (envGuestId) return envGuestId

  const storageKey = 'guestId'
  const savedGuestId = localStorage.getItem(storageKey)?.trim()
  if (savedGuestId) return savedGuestId

  const generatedGuestId = crypto.randomUUID()
  localStorage.setItem(storageKey, generatedGuestId)
  return generatedGuestId
}

function mapCartItem(item: CartItemResponse): CartItem {
  return {
    id: String(item.productId),
    productName: item.productName,
    productType: 'SINGLE_ORIGIN',
    price: item.price,
    quantity: item.quantity,
    imageSrc: productImageSrcByName[item.productName] ?? '/coffeeicon.png',
  }
}

export async function fetchCartItems(): Promise<CartItem[]> {
  await delay(0)
  return cloneItems(cart)
}

export async function setCartItemQuantity(
  id: string,
  quantity: number,
): Promise<CartItem[]> {
  await delay(0)

  const target = cart.find((item) => item.id === id)
  if (!target) throw new Error('장바구니 아이템을 찾을 수 없습니다.')

  const next = Math.max(1, Math.floor(quantity))
  target.quantity = next

  return cloneItems(cart)
}

export async function deleteCartItem(id: string): Promise<CartItem[]> {
  const productId = id;
  console.log("productId: " + productId);
  if (Number.isNaN(productId)) {
    throw new Error('상품 ID가 올바르지 않습니다.')
  }

  const guestId = resolveGuestId()
  const response = await fetch(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/carts/products/${productId}`,
    {
      method: 'DELETE',
      headers: {
        'X-Guest-Id': guestId,
      },
    }
  )

  if (!response.ok) {
    throw new Error('장바구니 상품 삭제 실패')
  }

  const result: CartResponse = await response.json()
  if (!result.success) {
    throw new Error(result.message || '장바구니 상품 삭제 실패')
  }

  return (result.data.cartItems ?? []).map(mapCartItem)
}

export async function clearCart(): Promise<void> {
  const guestId = resolveGuestId()
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/v1/carts/products`, {
    method: 'DELETE',
    headers: {
      'X-Guest-Id': guestId,
    },
  })

  if (!response.ok) {
    throw new Error('장바구니 전체 삭제 실패')
  }

  const result: CartResponse = await response.json()
  if (!result.success) {
    throw new Error(result.message || '장바구니 전체 삭제 실패')
  }
}

export async function addCartItem(item: CartItem): Promise<CartItem[]> {
  await delay(0)

  const exists = cart.find((cartItem) => cartItem.id === item.id)
  if (exists) {
    return cloneItems(cart)
  }

  cart = [...cart, { ...item, quantity: 1 }]
  return cloneItems(cart)
}