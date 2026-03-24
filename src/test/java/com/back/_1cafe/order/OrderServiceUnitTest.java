package com.back._1cafe.order;

import com.back._1cafe.cart.Cart;
import com.back._1cafe.cart.CartItem;
import com.back._1cafe.cart.CartRepository;
import com.back._1cafe.cart.CartService;
import com.back._1cafe.customer.Customer;
import com.back._1cafe.customer.CustomerRepository;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import com.back._1cafe.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // @JUnit <> @Mock을 연결시켜주는 어노테이션
public class OrderServiceUnitTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartService cartService;

    @InjectMocks
    private OrderService orderService; // Mock들이 주입된 서비스

    @Test
    @DisplayName("주문 생성 성공: 장바구니에 아이템이 있고 Form을 정상적으로 받으면 주문이 생성됨")
    void createOrder_Success() {
        // Given (준비)
        String guestId = "user1";
        OrderRequestBody request = new OrderRequestBody(
                "tester@test.com",
                "서울시 강남구 테헤란로 12443",
                "12345");

        Customer customer = new Customer(request.email());
        Product product = new Product("커피 이름", 10000, "커피 설명");
        Cart cart = new Cart(guestId);
        CartItem item = new CartItem(cart, product, 2);
        cart.getCartItemList().add(item);

        // Mock들의 행동 정의 (Stubbing)
        given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.of(cart));

        // When (실행)
        Orders orderResult = orderService.createOrder(guestId, request);

        // Then (검증)
        assertThat(orderResult.getCustomer().getEmail()).isEqualTo("tester@test.com");
        assertThat(orderResult.getOrderItems()).hasSize(1);
        assertThat(orderResult.getTotalPrice()).isEqualTo(20000); // 10000 * 2

        // 중요: 의존성 메서드가 호출되었는지 확인
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(cartService, times(1)).clearCart(guestId);
    }

    @Test
    @DisplayName("주문 실패: 장바구니가 없으면 CartNotFoundException이 발생")
    void createOrder_Fail_NoCart() {
        // Given: 테스트에 필요한 객체와 가짜 행동 설정
        String guestId = "user1";

        OrderRequestBody request = new OrderRequestBody(
                "tester@test.com",
                "서울시 강남구 테헤란로 124",
                "12345"
        );

        // 가짜 객체(Mock)가 아무것도 반환하지 않도록 설정
        given(cartRepository.findByGuestId(guestId)).willReturn(Optional.empty());

        // When & Then: 실제 호출할 때는 위에서 만든 request 객체를 전달
        assertThatThrownBy(() -> orderService.createOrder(guestId, request))
                .isInstanceOf(CartNotFoundException.class);
    }

    @Test
    @DisplayName("배치 계산 로직: 오후 2시 기준 전후로 배치가 정확히 나뉘는지 확인")
    void setDeliveryBatch_Test() {
        // 1. 오후 2시 이전 (예: 2026-03-24 13:59) -> 전날인 20260323 배치여야 함
        LocalDateTime morning = LocalDateTime.of(2026, 3, 24, 13, 59);
        int morningBatch = orderService.setDeliveryBatch(morning);
        assertThat(morningBatch).isEqualTo(20260323);

        // 2. 오후 2시 이후 (예: 2026-03-24 14:01) -> 당일인 20260324 배치여야 함
        LocalDateTime afternoon = LocalDateTime.of(2026, 3, 24, 14, 1);
        int afternoonBatch = orderService.setDeliveryBatch(afternoon);
        assertThat(afternoonBatch).isEqualTo(20260324);
    }
}