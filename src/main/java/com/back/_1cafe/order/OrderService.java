package com.back._1cafe.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public void create() {
//        1. 장바구니 조회 (X-Guest-Id)

//        2. 상품 존재 확인

//        3. 총 금액 계산

//        4. deliveryDate + batch 계산

//        5. 주문 생성 (Order)

//        6. 주문 상세 생성 (OrderItem)

//        7. 장바구니 비우기

    }
}
