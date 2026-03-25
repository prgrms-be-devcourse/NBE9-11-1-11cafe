package com.back._1cafe.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CartIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String guestId = "test123";

    @Test
    @DisplayName("t1: 빈 장바구니 조회 ")
    void t1() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/carts")
                        .header("X-Guest-Id", guestId)
        ).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cartItems").isEmpty());
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    void t2() throws Exception {
        CartDto.Request request = new CartDto.Request(1, 2);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/carts")
                        .header("X-Guest-Id", guestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cartItems[0].productId").value(1))
                .andExpect(jsonPath("$.data.cartItems[0].quantity").value(2));
    }

    @Test
    @DisplayName("이미 데이터가 들어있는 장바구니 조회")
    void t3() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/carts")
                        .header("X-Guest-Id", "user1")
        ).andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.guestId").value("user1"))
                .andExpect(jsonPath("$.data.cartItems").isNotEmpty());
    }

    @Test
    @DisplayName("장바구니 상품 수량 수정")
    void t4() throws Exception {
        // Given
        CartDto.UpdateQuantityReq request = new CartDto.UpdateQuantityReq(10);

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/carts/products/2")
                        .header("X-Guest-Id", "user2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("장바구니 수정 성공"))
                .andExpect(jsonPath("$.data.cartItems[0].quantity").value(10));
    }

    @Test
    @DisplayName("장바구니 개별 상품 삭제")
    void t5() throws Exception {
        // When:
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/carts/products/1")
                        .header("X-Guest-Id", "user1")
        ).andDo(print());

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("상품이 장바구니에서 삭제되었습니다."))
                .andExpect(jsonPath("$.data.cartItems").isEmpty());
    }

    @Test
    @DisplayName("장바구니 전체 삭제")
    void t6() throws Exception {

        // When:
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/carts")
                        .header("X-Guest-Id", "user2")
        ).andDo(print());

        // Then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("장바구니가 비워졌습니다."))
                .andExpect(jsonPath("$.data.cartItems").isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 상품을 장바구니에 추가 시도")
    void t7() throws Exception {
        // Given:
        CartDto.Request request = new CartDto.Request(999, 1);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/carts")
                        .header("X-Guest-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // Then:
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("999번 상품은 존재하지 않는 상품입니다."));
    }

    @Test
    @DisplayName("내 장바구니에 없는 상품 수량 수정 시도")
    void t8() throws Exception {
        // Given
        CartDto.UpdateQuantityReq request = new CartDto.UpdateQuantityReq(10);

        // When
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/carts/products/2")
                        .header("X-Guest-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // Then:
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("장바구니에 해당 상품이 존재하지 않습니다."));
    }


}
