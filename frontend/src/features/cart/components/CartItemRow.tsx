import type { CartItem } from '../cartTypes'

type Props = {
  item: CartItem
  onQuantityChange: (nextQuantity: number) => void
}

export function CartItemRow({ item, onQuantityChange }: Props) {
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
          onClick={() => onQuantityChange(item.quantity + 1)}
          aria-label={`${item.productName} 추가`}
        >
          추가
        </button>
      </div>
    </div>
  )
}