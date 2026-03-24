package com.back._1cafe.order;

import java.time.LocalDateTime;
import java.util.List;

/*
OrderDot에 필요한 내용만 넘기도록 수정
 */
public record OrderDto(
        Long orderId,
        String email,
        String address,
        String postcode,
        int deliveryBatch,
        int totalAmount,
        List<OrderItemDto> orderItems,
        LocalDateTime createdAt
) {
    public OrderDto(Orders orders) {
        this(
                orders.getId(),
                orders.getCustomer().getEmail(),
                orders.getAddress(),
                orders.getPostcode(),
                orders.getDeliveryBatch(),
                orders.getTotalPrice(),
                orders.getOrderItems().stream()
                        .map(OrderItemDto::new)
                        .toList(),
                orders.getCreatedAt()
        );
    }
}