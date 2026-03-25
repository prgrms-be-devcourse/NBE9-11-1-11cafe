package com.back._1cafe.order;

import com.back._1cafe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "OrderController", description = "주문 API")
public class OrderController {

    private final OrderService orderService;

    /*
        GuestId를 Body로 받는거 -> Header로수정
        Api명세서와 같은 응답이 나오도록 Dto수정 및 RsData.of 사용
     */
    @PostMapping("/orders")
    public RsData<OrderDto> createOrder(@RequestHeader("X-Guest-Id") String guestId, @RequestBody @Valid OrderRequestBody orderRequestBody) {
        Orders orders = this.orderService.createOrder(guestId,orderRequestBody);
        OrderDto orderDto = new OrderDto(orders);
        return RsData.of(
                "%d번 주문이 완료되었습니다.".formatted(orders.getId()),orderDto);
    }
}
