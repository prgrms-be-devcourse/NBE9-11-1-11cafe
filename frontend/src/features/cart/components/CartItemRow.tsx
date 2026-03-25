import type { CartItem } from '../cartTypes'

type Props = {
  item: CartItem
  onQuantityChange: (id: string) => void
  onShowToast: (toast: {
    message: string
    type: 'success' | 'error'
  } | null) => void
}

export function CartItemRow({ item, onQuantityChange, onShowToast }: Props) {
  const productTypeLabel =
    item.productType === 'SINGLE_ORIGIN' ? '단일원산지' : '블렌드'

  const handleAddToCart = async () => {
    if (item.quantity > 0) {
      onShowToast({
        message: `${item.productName} 상품은 이미 장바구니에 담겨 있습니다.`,
        type: 'error',
      })
      return
    }

    const productId = Number(item.id)
    if (Number.isNaN(productId)) {
      onShowToast({
        message: '상품 ID가 올바르지 않아 장바구니에 추가할 수 없습니다.',
        type: 'error',
      })
      return
    }

    try {
      await onQuantityChange(item.id)

      onShowToast({
        message: `${item.productName} 상품이 장바구니에 추가되었습니다.`,
        type: 'success',
      })
    } catch {
      onShowToast({
        message: `${item.productName} 상품 추가 중 오류가 발생했습니다.`,
        type: 'error',
      })
    }
  }

  return (
    <div className="cart-item-row" role="listitem">
      <div className="cart-item-thumb" aria-hidden="true">
        <img className="cart-item-thumb-img" src={item.imageSrc} alt="" />
      </div>

      <div className="cart-item-main">
        <div className="cart-item-name">{item.productName}</div>
        <div className="cart-item-sub">{productTypeLabel}</div>
      </div>

      <div className="cart-item-side">
        <div className="cart-item-price">{item.price.toLocaleString()}원</div>

        <div className="cart-item-action">
          <button
            type="button"
            className="cart-add-btn"
            onClick={() => {
              void handleAddToCart()
            }}
          >
            추가
          </button>
        </div>
      </div>
    </div>
  )
}