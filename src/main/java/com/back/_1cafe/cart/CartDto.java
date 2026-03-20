package com.back._1cafe.cart;

import java.util.List;

// 응답 데이터 전체를 담는 레코드
public record CartDto(
        int cartId,
        String guestId,
        List<CartItemDto> cartItems,
        int totalAmount
) {
    // 요청 데이터를 받기 위한 내부 레코드
    public record Request(
            int productId,
            int quantity
    ) {}

    // 장바구니에 담긴 개별 상품 정보를 위한 내부 레코드
    public record CartItemDto(
            int cartItemId,
            int productId,
            String productName,
            int price,
            int quantity,
            int itemTotal
    ) {}
}