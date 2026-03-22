package com.back._1cafe.cart;

import com.back._1cafe.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    //카트에 상품추가
    @PostMapping
    public RsData<CartDto> addProduct(
            @RequestHeader("X-Guest-Id") String guestId,
            @Valid @RequestBody CartDto.Request request
    ){
        CartDto rst=cartService.addProduct(guestId,request);
        return RsData.of("장바구니 추가 성공",rst);
    }
}
