import type { CartItem } from '../cartTypes'
import { CartItemRow } from './CartItemRow'

type Props = {
  items: CartItem[]
  mutating: boolean
  onQuantityChange: (id: string, nextQuantity: number) => void
  onShowToast: (toast: {
    message: string
    type: 'success' | 'error'
  } | null) => void
}

export function CartList({
  items,
  mutating,
  onQuantityChange,
  onShowToast,
}: Props) {
  return (
    <div className="cart-list" role="list">
      {items.map((item) => (
        <CartItemRow
          key={item.id}
          item={item}
          onQuantityChange={(nextQuantity) =>
            onQuantityChange(item.id, nextQuantity)
          }
          onShowToast={onShowToast}
        />
      ))}

      {items.length === 0 ? (
        <div className="cart-empty">장바구니가 비어 있어요.</div>
      ) : null}

      <div
        className="cart-muted-loading"
        style={{ visibility: mutating ? 'visible' : 'hidden' }}
        aria-live="polite"
      >
        처리 중...
      </div>
    </div>
  )
}

