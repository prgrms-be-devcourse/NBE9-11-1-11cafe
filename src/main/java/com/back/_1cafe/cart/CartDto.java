package com.back._1cafe.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
            @NotNull(message = "상품 ID는 필수입니다.")
            @Min(value = 1, message = "상품 ID는 1 이상이어야 합니다.")
            Integer productId,

            @NotNull(message = "수량은 필수입니다.")
            @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
            Integer quantity
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

    public record UpdateQuantityReq(
      @NotNull(message = "변경할 수량은 필수입니다.")
      @Min(value = 1,message = "수량은 1이상이여야 합니다.")
      Integer quantity
    ){}
}