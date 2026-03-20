package com.back._1cafe.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Getter
    @Setter
    public static class CreateOrderRequest {
        private String email;
        private String address;
        private String postcode;
    }

    @Getter
    @Setter
    public static class OrderItemResponse {
        private Integer productId;
        private Integer quantity;
    }

    @Getter
    @Setter
    public static class CreateOrderResponse {
        private Integer orderId;
        private String email;
        private Integer deliveryBatch;
        private Integer totalAmount;
        private List<OrderItemResponse> orderItems;
        private LocalDateTime createdAt;
    }
}
