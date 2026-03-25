import type { CartItem, CartTotals } from '../cartTypes'

type Props = {
  items: CartItem[]
  totals: CartTotals
  mutating: boolean
  onQuantityChange: (id: string, nextQuantity: number) => void
  onRemove: (id: string) => void
  onClearAll: () => void
}

export function CartTotals({
  items,
  totals,
  mutating,
  onQuantityChange,
  onRemove,
  onClearAll,
}: Props) {
  const safeItems = items ?? []

  return (
    <div className="cart-totals">
      <div className="cart-summary-top">
        <button
          type="button"
          className="cart-clear-all-btn"
          onClick={onClearAll}
          disabled={mutating || safeItems.length === 0}
        >
          전체 삭제
        </button>
      </div>

      <div className="cart-summary-items">
        {safeItems.map((item) => (
          <div className="cart-summary-item" key={item.id}>
            <div className="cart-summary-item-left">
              <span className="cart-summary-item-name">{item.productName}</span>
            </div>

            <div className="cart-summary-item-right">
              <button
                type="button"
                className="cart-summary-qty-btn"
                onClick={() => {
                  if (item.quantity === 1) {
                    onRemove(item.id)
                    return
                  }

                  onQuantityChange(item.id, item.quantity - 1)
                }}
                disabled={mutating}
                aria-label="수량 감소"
              >
                -
              </button>

              <span className="cart-summary-qty-badge" aria-label="수량">
                {item.quantity}개
              </span>

              <button
                type="button"
                className="cart-summary-qty-btn"
                onClick={() => onQuantityChange(item.id, item.quantity + 1)}
                disabled={mutating}
                aria-label="수량 증가"
              >
                +
              </button>

              <button
                type="button"
                className="cart-summary-delete-btn"
                onClick={() => onRemove(item.id)}
                disabled={mutating}
              >
                삭제
              </button>
            </div>
          </div>
        ))}
      </div>

      <div className="cart-summary-note">
        <span>당일 오후 2시 이후 주문은 다음날 배송</span>
      </div>

      <div className="cart-totals-row cart-totals-total">
        <span>총금액</span>
        <span>{totals.subtotal.toLocaleString('ko-KR')}원</span>
      </div>
    </div>
  )
}