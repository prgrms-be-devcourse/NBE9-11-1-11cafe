import { useMemo, useState } from 'react'
import { CartList } from '../../features/cart/components/CartList'
import { CartTotals } from '../../features/cart/components/CartTotals'
import { useCart } from '../../features/cart/hooks/useCart'
import './cartPage.css'

export function CartPage() {
  const {
    items,
    totals,
    loading,
    mutating,
    error,
    refresh,
    changeQuantity,
    deleteOne,
    deleteAll,
  } = useCart()

  const [email, setEmail] = useState('')
  const [address, setAddress] = useState('')
  const [postalCode, setPostalCode] = useState('')

  const isEmpty = useMemo(() => !loading && items.length === 0, [loading, items])
  const canCheckout =
    items.length > 0 &&
    !mutating &&
    email.trim().length > 0 &&
    address.trim().length > 0 &&
    postalCode.trim().length > 0

    return (
      <div className="cart-outer">
        <div className="cart-shell">
          <h1 className="cart-title">
            <span className="cart-brand">11cafe</span>
            <span className="cart-subtitle">커피 원두 주문</span>
          </h1>
    
          <div className="cart-grid">
          <section className="cart-left">
            <div className="cart-left-header">
              <h2 className="cart-left-title">상품 목록</h2>
            </div>

            {error ? <div className="cart-error">{error}</div> : null}
            {loading ? <div className="cart-loading">목록을 불러오는 중...</div> : null}
            {isEmpty ? <div className="cart-empty-state">장바구니가 비어 있어요.</div> : null}

            {!loading && items.length > 0 ? (
              <CartList
                items={items}
                mutating={mutating}
                onQuantityChange={changeQuantity}
              />
            ) : null}
          </section>

          <aside className="cart-right">
            <div className="cart-right-inner">
              <h2 className="cart-right-title">Summary</h2>
              <CartTotals
                items={items}
                totals={totals}
                mutating={mutating}
                onQuantityChange={changeQuantity}
                onRemove={deleteOne}
                onClearAll={deleteAll}
              />

              <div className="cart-form">
                <label className="cart-field">
                  <span className="cart-label">이메일</span>
                  <input
                    className="cart-input"
                    type="email"
                    placeholder="you@example.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                </label>

                <label className="cart-field">
                  <span className="cart-label">주소</span>
                  <input
                    className="cart-input"
                    type="text"
                    placeholder="배송 주소를 입력하세요"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                  />
                </label>

                <label className="cart-field">
                  <span className="cart-label">우편번호</span>
                  <input
                    className="cart-input"
                    type="text"
                    placeholder="우편번호"
                    value={postalCode}
                    onChange={(e) => setPostalCode(e.target.value)}
                  />
                </label>

                <button
                  type="button"
                  className="cart-checkout-btn"
                  disabled={!canCheckout}
                  onClick={() => {
                    // 학습용: 결제 로직은 아직 연결하지 않습니다.
                    refresh()
                  }}
                >
                  결제하기
                </button>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </div>
  )
}

