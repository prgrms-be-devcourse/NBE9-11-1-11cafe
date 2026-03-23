package com.back._1cafe.cart;

import com.back._1cafe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
            @Valid @RequestBody CartDto.Request request
    ) {
        CartDto rst = cartService.addProduct(guestId, request);
        return RsData.of("장바구니 추가 성공", rst);
    }

    //장바구니 상품 수량변경
    @PutMapping("/products/{productId}")
    public RsData<CartDto> modifyProduct(
            @RequestHeader("X-Guest-Id") String guestId,
            @PathVariable Integer productId,
            @Valid @RequestBody CartDto.UpdateQuantityReq req
    ){
        CartDto rst = cartService.modifyProduct(guestId,productId,req.quantity());
        return RsData.of("장바구니 수정 성공", rst);
    }

    @Operation(summary = "장바구니 전체 삭제")
    @DeleteMapping("")
    public RsData<CartDto> deleteAllCart(
            @RequestHeader("X-Guest-Id") String guestId
    ) {
        CartDto cart = cartService.getCart(guestId);

        if (cart.cartItems().isEmpty()) {
            return RsData.fail("이미 장바구니가 비워져 있습니다.");
        }

        CartDto rst = cartService.clearCart(guestId);

        return RsData.of("장바구니가 비워졌습니다.", rst);
    }

    //장바구니 상품 개별 삭제
    @DeleteMapping("/products/{productId}")
    public RsData<CartDto> deleteProduct(
            @RequestHeader("X-Guest-Id") String guestId,
            @PathVariable Integer productId
    ){
        CartDto rst = cartService.deleteProduct(guestId,productId);
        return RsData.of("상품이 장바구니에서 삭제되었습니다.",rst);
    }
}
