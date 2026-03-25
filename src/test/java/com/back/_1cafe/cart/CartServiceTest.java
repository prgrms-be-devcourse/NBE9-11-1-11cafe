package com.back._1cafe.cart;

import com.back._1cafe.global.exception.customExcetpion.CartItemNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.ProductNotFoundException;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @InjectMocks
    private CartService cartService;
    @Mock
    CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("장바구니 조회성공")
    void t1(){
        //Given
        String  guestId="user123";
        Cart cart =new Cart(guestId);
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        //when
        CartDto rst = cartService.getCart(guestId);
        //then
        assertThat(rst.guestId()).isEqualTo("user123");
        assertThat(rst.totalAmount()).isEqualTo(0);
    }
    @Test
    @DisplayName("장바구니가 존재하지 않을 때 빈 장바구니 객체를 반환한다")
    void t2() {
        // Given
        String guestId = "user123";

        // Repository가 빈 Optional을 반환하도록 설정
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.empty());

        // When
        CartDto result = cartService.getCart(guestId);

        // Then
        // 1. 결과가 null이 아니어야 함
        Assertions.assertThat(result).isNotNull();
        // 2. 해당 사용자의 guestId를 가지고 있어야 함
        Assertions.assertThat(result.guestId()).isEqualTo(guestId);
        // 3. 상품 목록(items)은 비어 있어야 함 (빈 배열 반환 검증)
        Assertions.assertThat(result.cartItems()).isEmpty();
        // 4. 총 금액은 0원이어야 함
        Assertions.assertThat(result.totalAmount()).isEqualTo(0);
    }
    @Test
    @DisplayName("상품 추가 성공")
    void t3(){
        //given
        String guestId = "user123";
        Cart cart = new Cart(guestId);
        Product product= new Product("아메리카노",5000,"물탄커피");
        CartDto.Request request=new CartDto.Request(1,2);

        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        //when
        CartDto rst = cartService.addProduct(guestId,request);
        // Then
        assertThat(rst.cartItems()).hasSize(1);
        assertThat(rst.cartItems().get(0).quantity()).isEqualTo(2);
        assertThat(rst.totalAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("이미 있는 상품 또 담을때")
    void t4(){
        //given
        String guestId = "user123";
        Cart cart = new Cart(guestId);
        Product product= new Product("아메리카노",5000,"물탄커피");

        cart.getCartItemList().add(new CartItem(cart,product,2));

        CartDto.Request request=new CartDto.Request(1,3);
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        //when
        CartDto rst = cartService.addProduct(guestId,request);
        // Then
        assertThat(rst.cartItems()).hasSize(1);
        assertThat(rst.cartItems().get(0).quantity()).isEqualTo(5);
        assertThat(rst.totalAmount()).isEqualTo(25000);
    }
    @Test
    @DisplayName("존재하지 않는 상품을 담을때")
    void t5() {
        String guestId = "use123";
        Cart cart = new Cart(guestId);
        int productId = 100;
        CartDto.Request req = new CartDto.Request(productId, 1);
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> cartService.addProduct(guestId, req))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("%d번 상품은 존재하지 않는 상품입니다.".formatted(productId));
    }
    @Test
    @DisplayName("상품수량변경")
    void t6(){
        //Given
        String guestId = "user123";
        Cart cart = new Cart(guestId);
        Product product = new Product("아메리카노",5000,"물탄커피에요");
        int targetId = product.getProductId();
        CartItem cartItem = new CartItem(cart,product,2);
        cart.getCartItemList().add(cartItem);
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        //when
        int modifyQuantity=5;
        CartDto rst = cartService.modifyProduct(guestId,0,modifyQuantity);
        //then
        assertThat(rst.cartItems().get(targetId).quantity()).isEqualTo(5);
        assertThat(rst.totalAmount()).isEqualTo(25000);
    }
    @Test
    @DisplayName("상품 수량 변경 실패")
    void t7(){
        // Given
        String guestId = "user123";
        Cart cart = new Cart(guestId);

        int targetProductId = 999;
        int modifyQuantity = 5;

        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        // When & Then
        assertThatThrownBy(() -> cartService.modifyProduct(guestId, targetProductId, modifyQuantity))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("장바구니에 해당 상품이 존재하지 않습니다.");
    }
    @Test
    @DisplayName("장바구니 개별 삭제 - 2개의 상품 중 1개만 삭제")
    void t8() {
        // Given
        String guestId = "user123";
        Cart cart = new Cart(guestId);

        Product product1 = new Product("아메리카노", 5000, "물탄 커피");
        ReflectionTestUtils.setField(product1, "productId", 1);

        Product product2 = new Product("카페라떼", 6000, "우유 탄 커피");
        ReflectionTestUtils.setField(product2, "productId", 2);

        cart.getCartItemList().add(new CartItem(cart, product1, 2));
        cart.getCartItemList().add(new CartItem(cart, product2, 1));

        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));

        // When
        int targetProductId = product1.getProductId();
        CartDto rst = cartService.deleteProduct(guestId, targetProductId);

        // Then
        assertThat(rst.cartItems()).hasSize(1);

        assertThat(rst.cartItems().get(0).productId()).isEqualTo(2);
        assertThat(rst.cartItems().get(0).productName()).isEqualTo("카페라떼");

        assertThat(rst.totalAmount()).isEqualTo(6000);
    }

    @Test
    @DisplayName("개별 상품 삭제 실패 - 장바구니가 존재하지 않을 때")
    void t11() {
        // Given
        String notExistGuestId = "user123";
        int targetProductId = 1;
        given(cartRepository.findByGuestId(notExistGuestId)).willReturn(Optional.empty());
        // When & Then
        assertThatThrownBy(() -> cartService.deleteProduct(notExistGuestId, targetProductId))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage("장바구니가 존재하지않습니다.");
    }
    @Test
    @DisplayName("개별 상품 삭제 실패 - 장바구니에 해당 상품이 존재하지 않을 때")
    void t12() {
        String guestId = "user123";
        Cart cart = new Cart(guestId);
        int notExistProductId = 999;

        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));

        // When & Then
        assertThatThrownBy(() -> cartService.deleteProduct(guestId, notExistProductId))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage("장바구니에 해당 상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("장바구니 전체 비우기 성공")
    void t9_clearCart_success() {
        String guestId = "user-123";
        Cart cart = new Cart(guestId);

        Product product1 = new Product("아메리카노", 5000, "물탄 커피");
        Product product2 = new Product("카페라떼", 6000, "우유 탄 커피");
        cart.getCartItemList().add(new CartItem(cart, product1, 2));
        cart.getCartItemList().add(new CartItem(cart, product2, 1));
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));
        // When
        CartDto result = cartService.clearCart(guestId);

        // Then
        assertThat(result.cartItems()).isEmpty();
        assertThat(result.totalAmount()).isEqualTo(0);
    }

}
