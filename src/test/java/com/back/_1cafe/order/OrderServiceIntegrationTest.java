package com.back._1cafe.order;

import com.back._1cafe.cart.Cart;
import com.back._1cafe.cart.CartItem;
import com.back._1cafe.cart.CartRepository;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;

    @Test
    @DisplayName("통합 주문 성공: 실제 DB와 연동하여 장바구니 데이터를 기반으로 주문 생성")
    void createOrder_Integration_Success() {
        // Given: 실제 DB에 필요한 데이터 저장
        Product product = productRepository.save(new Product("커피 이름99", 5000, "커피 설명99"));
        String guestId = "user1_";

        // 장바구니 생성 및 아이템 추가
        Cart cart = cartRepository.save(new Cart(guestId));
        CartItem cartItem = new CartItem(cart, product, 3);
        cart.getCartItemList().add(cartItem);

        OrderRequestBody orderRequest = new OrderRequestBody(
                "tester_@test.com",
                "서울시 강남구 테헤란로 12443",
                "12345"
        );

        // When: 실제 서비스 메서드 실행
        Orders savedOrder = orderService.createOrder(guestId, orderRequest);

        // deliveryBatch 테스트 위해 현재 시각 변수 저장
        LocalDateTime referenceTime = LocalDateTime.now();

        // Then: DB 상태 검증
        // 주문 저장 되었는지 확인
        Orders foundOrder = orderRepository.findById(savedOrder.getId()).orElseThrow();
        assertThat(foundOrder.getTotalPrice()).isEqualTo(15000);
        assertThat(foundOrder.getCustomer().getEmail()).isEqualTo("tester_@test.com");

        // 장바구니가 지워졌는지 확인
        Optional<Cart> clearedCart = cartRepository.findByGuestId(guestId);
        assertThat(clearedCart.get().getCartItemList()).isEmpty();

        // deliveryBatch 잘 들어갔는지 확인
        assertThat(savedOrder.getDeliveryBatch())
                .isEqualTo(Integer.parseInt(referenceTime.minusHours(14).format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    }
}
