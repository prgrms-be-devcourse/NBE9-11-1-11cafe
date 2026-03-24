import { useCallback, useEffect, useMemo, useState } from 'react'
import type { CartItem, CartTotals } from '../cartTypes'
import {
  clearCart,
  deleteCartItem,
  fetchCartItems,
  setCartItemQuantity,
} from '../cartService'

type CartError = string | null

export function useCart() {
  const [items, setItems] = useState<CartItem[]>([])
  const [loading, setLoading] = useState(false)
  const [mutating, setMutating] = useState(false)
  const [error, setError] = useState<CartError>(null)

  const totals: CartTotals = useMemo(() => {
    const itemCount = items.reduce((sum, item) => sum + item.quantity, 0)
    const subtotal = items.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0,
    )
    return { itemCount, subtotal }
  }, [items])

  const refresh = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const nextItems = await fetchCartItems()
      setItems(nextItems)
    } catch (e) {
      setError(e instanceof Error ? e.message : '장바구니 조회 실패')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    refresh()
  }, [refresh])

  const changeQuantity = useCallback(
    async (id: string, quantity: number) => {
      setMutating(true)
      setError(null)
      try {
        const next = await setCartItemQuantity(id, quantity)
        setItems(next)
      } catch (e) {
        setError(e instanceof Error ? e.message : '수량 변경 실패')
      } finally {
        setMutating(false)
      }
    },
    [],
  )

  const deleteOne = useCallback(async (id: string) => {
    setMutating(true)
    setError(null)
    try {
      const next = await deleteCartItem(id)
      setItems(next)
    } catch (e) {
      setError(e instanceof Error ? e.message : '삭제 실패')
    } finally {
      setMutating(false)
    }
  }, [])

  const deleteAll = useCallback(async () => {
    setMutating(true)
    setError(null)
    try {
      await clearCart()
      setItems([])
    } catch (e) {
      setError(e instanceof Error ? e.message : '전체 삭제 실패')
    } finally {
      setMutating(false)
    }
  }, [])

  return {
    items,
    totals,
    loading,
    mutating,
    error,
    refresh,
    changeQuantity,
    deleteOne,
    deleteAll,
  }
}

