import type { CartItem } from './cartTypes'
import { productImageSrcByName } from './cartProductImages'

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

const GUEST_ID_STORAGE_KEY = 'guestId'
const DEFAULT_GUEST_ID = 'user-100'


export function resolveGuestId() {
  const saved = localStorage.getItem(GUEST_ID_STORAGE_KEY)?.trim()
  if (saved) return saved

  localStorage.setItem(GUEST_ID_STORAGE_KEY, DEFAULT_GUEST_ID)
  return DEFAULT_GUEST_ID
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
  const guestId = resolveGuestId()
  

  const response = await fetch(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/carts`,
    {
      method: 'GET',
      headers: {
        'X-Guest-Id': guestId,
      },
    }
  )

  console.log("response: " + response)

  if (!response.ok) {
    throw new Error('장바구니 조회 실패')
  }

  const result: CartResponse = await response.json()
  console.log("result 출력: " + result);

  if (!result.success) {
    throw new Error(result.message || '장바구니 조회 실패')
  }

  return (result.data.cartItems ?? []).map(mapCartItem)
}

export async function setCartItemQuantity(
  id: string,
  quantity: number,
): Promise<CartItem[]> {
  const productId = Number(id)

  if (Number.isNaN(productId)) {
    throw new Error('상품 ID가 올바르지 않습니다.')
  }

  const guestId = resolveGuestId()
  const nextQuantity = Math.max(1, Math.floor(quantity))

  const response = await fetch(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/carts/products/${productId}`,
    {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'X-Guest-Id': guestId,
      },
      body: JSON.stringify({
        quantity: nextQuantity,
      }),
    }
  )

  if (!response.ok) {
    throw new Error('장바구니 수량 변경 실패')
  }

  const result: CartResponse = await response.json()

  if (!result.success) {
    throw new Error(result.message || '장바구니 수량 변경 실패')
  }

  return (result.data.cartItems ?? []).map(mapCartItem)
}

export async function deleteCartItem(id: string): Promise<CartItem[]> {
  const productId = Number(id)
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
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/v1/carts`, {
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
  const productId = Number(item.id)

  if (Number.isNaN(productId)) {
    throw new Error('상품 ID가 올바르지 않습니다.')
  }

  const guestId = resolveGuestId()

  const response = await fetch(
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/carts`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Guest-Id': guestId,
      },
      body: JSON.stringify({
        productId,
        quantity: 1,
      }),
    }
  )

  if (!response.ok) {
    throw new Error('장바구니 상품 추가 실패')
  }

  const result: CartResponse = await response.json()

  if (!result.success) {
    throw new Error(result.message || '장바구니 상품 추가 실패')
  }

  return (result.data.cartItems ?? []).map(mapCartItem)
}