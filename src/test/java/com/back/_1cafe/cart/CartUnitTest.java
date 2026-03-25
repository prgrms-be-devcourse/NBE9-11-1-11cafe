package com.back._1cafe.cart;

import com.back._1cafe.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CartUnitTest {
    @Test
    @DisplayName("상품총 가격 테스트")
    void t1() {
        //Given
        Cart cart = new Cart("test123");
        Product product1 = new Product("아메리카노", 5000, "물탄 커피에요");
        Product product2 = new Product("카페라뗴", 7000, "우유가 들어간 커피에요");
        cart.getCartItemList().add(new CartItem(cart, product1, 3));
        cart.getCartItemList().add(new CartItem(cart, product2, 5));
        //When
        int totalAmount = cart.getTotalAmount();
        //Then
        Assertions.assertThat(50000).isEqualTo(totalAmount);
    }

    @Test
    @DisplayName("장바구니 삼풍 수량 누적 테스트")
    public void t2() {
        //Given
        Cart cart = new Cart("test1233");
        Product product = new Product("아메리카노", 5000, "물탄커피");
        CartItem cartItem = new CartItem(cart, product, 3);
        //when
        cartItem.addQuantity(2);
        //then
        Assertions.assertThat(5).isEqualTo(cartItem.getQuantity());
    }

}
