import type { CartItem } from '../cartTypes'

type Props = {
  item: CartItem
  onQuantityChange: (nextQuantity: number) => void
  onShowToast: (toast: {
    message: string
    type: 'success' | 'error'
  } | null) => void
}

export function CartItemRow({ item, onQuantityChange, onShowToast }: Props) {
  const productTypeLabel =
    item.productType === 'SINGLE_ORIGIN' ? '단일원산지' : '블렌드'

  return (
    <div className="cart-item-row" role="listitem">
      <div className="cart-item-thumb" aria-hidden="true">
        <img className="cart-item-thumb-img" src={item.imageSrc} alt="" />
      </div>

      <div className="cart-item-main">
        <div className="cart-item-name">{item.productName}</div>
        <div className="cart-item-sub">{productTypeLabel}</div>
      </div>

      <div className="cart-item-action">
        <button
          type="button"
          className="cart-add-btn"
          onClick={() => {
            if (item.quantity > 0) {
              onShowToast({
                message: `${item.productName} 상품은 이미 장바구니에 담겨 있습니다.`,
                type: 'error',
              })
              return
            }

            onShowToast({
              message: `${item.productName} 상품이 장바구니에 추가되었습니다.`,
              type: 'success',
            })
            onQuantityChange(item.quantity + 1)
          }}
        >
          추가
        </button>
      </div>
    </div>
  )
}