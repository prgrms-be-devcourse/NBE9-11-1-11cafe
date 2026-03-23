package com.back._1cafe.order;

import com.back._1cafe.product.Product;

public record OrderItemDto(
        Long id,
        Product product,
        int quantity
) {
    public OrderItemDto (OrderItem orderItem) {
        this(
                orderItem.getId(),
                orderItem.getProduct(),
                orderItem.getQuantity()
        );
    }
}