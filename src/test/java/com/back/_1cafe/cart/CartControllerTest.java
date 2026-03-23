package com.back._1cafe.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class CartControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("장바구니 상품 추가 성공")
    void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/carts")
                                .header("X-Guest-Id", "guest-test-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "productId": 1,
                                          "quantity": 1
                                        }
                                        """)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("장바구니 추가 성공"));
    }

    @Test
    @DisplayName("수량이 0이면 실패")
    void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/carts")
                                .header("X-Guest-Id", "guest-test-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "productId": 1,
                                          "quantity": 0
                                        }
                                        """)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("잘못된 JSON 형식이면 실패")
    void t3() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/carts")
                                .header("X-Guest-Id", "guest-test-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "productId": "abc",
                                      "quantity": 1
                                    }
                                    """)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 데이터 형식입니다."));
    }
}
