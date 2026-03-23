package com.back._1cafe.order;

import com.back._1cafe.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public RsData<OrderResponseBody> createOrder(@RequestBody @Valid OrderRequestBody orderRequestBody) {
        Orders orders = this.orderService.createOrder(orderRequestBody);

        return new RsData<>(
                "%d번 주문이 완료되었습니다.".formatted(orders.getId()),
                "201-1",
                new OrderResponseBody(
                        new OrderDto(orders)
                )
        );
    }
}
