package com.back._1cafe.cart;

import com.back._1cafe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("")
    @Operation(summary = "장바구니 상품 조회")
    public RsData<CartDto> listCart(
            @RequestHeader("X-Guest-Id") String guestId
    ){
        CartDto rst = cartService.getCart(guestId);
        return RsData.of("장바구니 조회 성공", rst);
    }

    //카트에 상품추가
    @PostMapping
    public RsData<CartDto> addProduct(
            @RequestHeader("X-Guest-Id") String guestId,
            @RequestBody CartDto.Request request
    ){
        //Todo:상품조회 완료시 진행가능
        //CartDto rst=cartService.addProduct(guestId,request);
        //return RsData.of("장바구니 추가 성공",rst);
        return null;
    }
}
