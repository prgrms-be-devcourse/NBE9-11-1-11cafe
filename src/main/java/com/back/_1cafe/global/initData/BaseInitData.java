package com.back._1cafe.global.initData;

import com.back._1cafe.cart.CartDto;
import com.back._1cafe.cart.CartService;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class BaseInitData implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product("Columbia",15000,"SINGLE_ORIGIN"),
                    new Product("Ethiopia", 17000,"SINGLE_ORIGIN"),
                    new Product("Brazil",13000, "BLENDED"),
                    new Product("Kenya", 16000,  "SINGLE_ORIGIN")
            ));
        }

        work1(); // 장바구니 초기 데이터값 세팅.
    }

    @Transactional
    public void work1() {
        if (cartService.count() > 0) {
            System.out.println("이미 존재함.");
            return;
        }

        Product product1 = productRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("상품1 없음"));

        Product product2 = productRepository.findById(2)
                .orElseThrow(() -> new IllegalArgumentException("상품2 없음"));

        CartDto.Request request1 = new CartDto.Request(product1.getProductId(), 3);
        CartDto.Request request2 = new CartDto.Request(product2.getProductId(), 5);

        cartService.addProduct("user1", request1);
        cartService.addProduct("user2", request2);
    }
}
