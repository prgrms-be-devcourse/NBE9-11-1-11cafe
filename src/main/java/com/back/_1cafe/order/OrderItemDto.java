package com.back._1cafe.order;

/*
Product를 통째로 넘기지 않고 필요한 필드만 꺼내도록 수정
 */
public record OrderItemDto(
        int productId,
        String productName,
        int quantity,
        int price
) {
    public OrderItemDto (OrderItem orderItem) {
        this(
                orderItem.getProduct().getProductId(),
                orderItem.getProduct().getProductName(),
                orderItem.getQuantity(),
                orderItem.getProduct().getPrice()
        );
    }
}