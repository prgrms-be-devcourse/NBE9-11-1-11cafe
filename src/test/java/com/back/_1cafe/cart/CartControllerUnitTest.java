package com.back._1cafe.cart;

import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class CartControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CartService cartService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("장바구니 상품 조회 성공")
    void t1() throws Exception {
        //given
        String guestId = "test123";
        CartDto res = new CartDto(1, guestId, List.of(), 0);
        given(cartService.getCart(guestId)).willReturn(res);
        //when&then
        mockMvc.perform(get("/api/v1/carts")
                        .header("X-Guest-Id", guestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("장바구니 조회 성공"));
    }

    @Test
    @DisplayName("장바구니 상품 추가 (및 생성) 성공")
    void t2() throws Exception {
        // Given
        String guestId = "test123";
        CartDto.Request requestDto = new CartDto.Request(1, 1);

        CartDto.CartItemDto item = new CartDto.CartItemDto(1, 1, "아메리카노", 5000, 1, 5000);
        CartDto mockResponse = new CartDto(1, guestId, List.of(item), 5000);

        given(cartService.addProduct(eq(guestId), any(CartDto.Request.class)))
                .willReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/carts")
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("장바구니 추가 성공"))
                .andExpect(jsonPath("$.data.cartItems[0].productName").value("아메리카노"));
    }

    @Test
    @DisplayName("장바구니 상품 추가 실패")
    void t3() throws Exception {
        // Given
        String guestId = "test123";
        CartDto.Request requestDto = new CartDto.Request(1, 0);

        // When & Then
        mockMvc.perform(post("/api/v1/carts")
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("수량은 1 이상이어야 합니다."));
    }

    @Test
    @DisplayName("수정성공")
    void t4() throws Exception {
        // Given
        String guestId = "test123";
        int productId = 1;
        int newQuantity = 5;

        CartDto.UpdateQuantityReq updateReq = new CartDto.UpdateQuantityReq(newQuantity);
        CartDto mockResponse = new CartDto(1, guestId, List.of(), 0);

        given(cartService.modifyProduct(guestId, productId, newQuantity)).willReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/carts/products/{productId}", productId)
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("장바구니 수정 성공"));
    }
    @Test
    @DisplayName("수정 실패 1 - 수량이 0인 경우")
    void t5() throws Exception {
        // Given
        String guestId = "test123";
        int productId = 1;

        // 💡 0을 넣어서 @Min(value = 1) 예외를 유도합니다.
        CartDto.UpdateQuantityReq invalidReq = new CartDto.UpdateQuantityReq(0);

        // When & Then
        mockMvc.perform(put("/api/v1/carts/products/{productId}", productId)
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400 에러 발생
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("수량은 1이상이여야 합니다.")); // DTO에 설정된 메시지
    }

    @Test
    @DisplayName("수정 실패 2 - 수량이 null인 경우 (누락)")
    void t6() throws Exception {
        // Given
        String guestId = "test123";
        int productId = 1;

        // 💡 null을 넣어서 @NotNull 예외를 유도합니다.
        CartDto.UpdateQuantityReq invalidReq = new CartDto.UpdateQuantityReq(null);

        // When & Then
        mockMvc.perform(put("/api/v1/carts/products/{productId}", productId)
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400 에러 발생
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("변경할 수량은 필수입니다.")); // DTO에 설정된 메시지
    }

    @Test
    @DisplayName("장바구니 개별삭제")
    void t7() throws Exception {
        // Given
        String guestId = "test123";
        int productId = 1;

        CartDto cart = new CartDto(1, guestId, List.of(),0);

        given(cartService.deleteProduct(guestId, productId)).willReturn(cart);

        // When & Then
        mockMvc.perform(delete("/api/v1/carts/products/{productId}", productId)
                        .header("X-Guest-Id", guestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품이 장바구니에서 삭제되었습니다."));
    }
    @Test
    @DisplayName("장바구니 전체삭제")
    void t8() throws Exception{
        String guestId = "test123";

        CartDto.CartItemDto item = new CartDto.CartItemDto(1, 1, "아메리카노", 5000, 1, 5000);
        CartDto nonEmptyCart = new CartDto(1, guestId, List.of(item), 5000);
        CartDto emptyCart = new CartDto(1, guestId, List.of(), 0);

        given(cartService.getCart(guestId)).willReturn(nonEmptyCart);
        given(cartService.clearCart(guestId)).willReturn(emptyCart);
        // When & Then
        mockMvc.perform(delete("/api/v1/carts")
                        .header("X-Guest-Id", guestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("장바구니가 비워졌습니다."));
    }

    @Test
    @DisplayName("장바구니가 존재하지않는경우")
    void t9() throws Exception {
        // Given
        String guestId = "test123";

        given(cartService.getCart(guestId)).willThrow(new CartNotFoundException("장바구니가 존재하지않습니다."));

        // When & Then
        mockMvc.perform(delete("/api/v1/carts")
                        .header("X-Guest-Id", guestId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("장바구니가 존재하지않습니다."));
    }
}
