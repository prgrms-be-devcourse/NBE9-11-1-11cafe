export type CartProductType = 'SINGLE_ORIGIN' | 'BLENDED'

export type CartItem = {
  id: string
  productName: string
  productType: CartProductType
  price: number // 1개 가격 (원)
  quantity: number
  imageSrc: string
}

export type CartTotals = {
  itemCount: number
  subtotal: number
}

