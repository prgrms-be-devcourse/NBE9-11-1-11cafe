package com.back._1cafe.order;

import com.back._1cafe.customer.Customer;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        Customer customer,
        LocalDateTime createdAt,
        List<OrderItemDto> orderItems,
        int batchId,
        int totalPrice
) {
    public OrderDto(Orders orders) {
        this(
                orders.getId(),
                orders.getCustomer(),
                orders.getCreatedAt(),
                orders.getOrderItems().stream()
                        .map(OrderItemDto::new)
                        .toList(),
                orders.getDeliveryBatch(),
                orders.getTotalPrice()
        );
    }
}