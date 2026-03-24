import { useEffect, useState } from 'react'
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
    addOne,
    changeQuantity,
    deleteOne,
    deleteAll,
  } = useCart()

  const [email, setEmail] = useState('')
  const [address, setAddress] = useState('')
  const [postalCode, setPostalCode] = useState('')
  const [toast, setToast] = useState<{
    message: string
    type: 'success' | 'error'
  } | null>(null)

  //Test
  const [products, setProducts] = useState<
    {
      id: string
      productName: string
      productType: 'SINGLE_ORIGIN' | 'BLENDED'
      price: number
      quantity: number
      imageSrc: string
    }[]
  >([])

  const handleAddProduct = (id: string) => {
    const product = products.find((item) => item.id === id)
    if (!product) return

    addOne(product)
  }

  useEffect(() => {
    if (!toast) return

    const timer = setTimeout(() => {
      setToast(null)
    }, 2000)

    return () => clearTimeout(timer)
  }, [toast])

// Test
useEffect(() => {
  fetch('/api/v1/products')
    .then((res) => {
      console.log('products status:', res.status)
      return res.json()
    })
    .then((result) => {
      console.log('products result:', result)

      const mappedProducts = result.data.map((product: any) => {
        const cartItem = items.find(
          (item) => item.productName === product.productName,
        )

        return {
          id: product.productId.toString(),
          productName: product.productName,
          productType: product.description,
          price: product.price,
          quantity: cartItem?.quantity ?? 0,
          imageSrc: '/coffeeicon.png',
        }
      })

      console.log('mappedProducts:', mappedProducts)
      setProducts(mappedProducts)
    })
    .catch((err) => {
      console.error('products error:', err)
    })
}, [items])
//--------

  const showToast = (nextToast: {
    message: string
    type: 'success' | 'error'
  } | null) => {
    setToast(null)

    if (!nextToast) return

    setTimeout(() => {
      setToast(nextToast)
    }, 0)
  }

  const canCheckout =
    items.length > 0 &&
    !mutating &&
    email.trim().length > 0 &&
    address.trim().length > 0 &&
    postalCode.trim().length > 0

    const handleCheckout = async () => {
      try {
        const response = await fetch('/api/v1/orders', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-Guest-id': localStorage.getItem('guestId') || 'guest-1',
          },
          body: JSON.stringify({
            email,
            address,
            postcode: postalCode,
          }),
        })
    
        const result = await response.json()
    
        console.log('order result:', result)
    
        if (!response.ok || !result.data) {
          throw new Error(result.message || '주문 실패')
        }
    
        // 장바구니 전체 삭제
        await deleteAll()
    
        await refresh()
    
        setEmail('')
        setAddress('')
        setPostalCode('')
    
        showToast({
          message: '주문이 완료되었습니다!',
          type: 'success',
        })
    
      } catch (err: any) {
        console.error('order error:', err)
    
        showToast({
          message: err.message || '주문 중 오류 발생',
          type: 'error',
        })
      }
    }

  return (
    <div className="cart-outer">
      {toast && (
        <div
          className={`cart-toast ${toast.type === 'success' ? 'cart-toast-success' : 'cart-toast-error'
            }`}
        >
          {toast.message}
        </div>
      )}

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

            <CartList
              items={products}
              mutating={mutating}
              onQuantityChange={(id) => handleAddProduct(id)}
              onShowToast={showToast}
            />
            
          </section>
          
          <aside className="cart-right">
            <div className="cart-right-inner">
              <h2 className="cart-right-title">Summary</h2>

              {loading ? <div className="cart-loading">장바구니를 불러오는 중...</div> : null}

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
                  onClick={handleCheckout}
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