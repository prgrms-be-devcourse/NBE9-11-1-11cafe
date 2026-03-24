import type { CartItem } from './cartTypes'
import { productImageSrcByName } from './cartProductImages'

// NOTE: 지금은 백엔드 대신 메모리 기반 목 데이터로 동작합니다.
let cart: CartItem[] = [
  {
    id: 'columbia',
    productName: 'Columbia',
    productType: 'SINGLE_ORIGIN',
    price: 15000,
    quantity: 2,
    imageSrc: productImageSrcByName['Columbia'],
  },
  {
    id: 'ethiopia',
    productName: 'Ethiopia',
    productType: 'SINGLE_ORIGIN',
    price: 17000,
    quantity: 1,
    imageSrc: productImageSrcByName['Ethiopia'],
  },
  {
    id: 'brazil',
    productName: 'Brazil',
    productType: 'BLENDED',
    price: 13000,
    quantity: 1,
    imageSrc: productImageSrcByName['Brazil'],
  },
  {
    id: 'kenya',
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
  await delay(0)
  cart = cart.filter((item) => item.id !== id)
  return cloneItems(cart)
}

export async function clearCart(): Promise<void> {
  await delay(0)
  cart = []
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