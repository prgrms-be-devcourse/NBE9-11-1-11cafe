package com.back._1cafe.global.initData;

import com.back._1cafe.cart.CartDto;
import com.back._1cafe.cart.CartService;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Bean
    public ApplicationRunner initData() {
        System.out.println("initData 호출 됨.");
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (cartService.count() > 0) {
            return;
        }

        Product product1 = productRepository.save(
                new Product("아메리카노", 3000, "기본 커피")
        );

        Product product2 = productRepository.save(
                new Product("라떼", 4000, "우유 커피")
        );

        CartDto.Request request1 = new CartDto.Request(product1.getProductId(), 3);
        CartDto.Request request2 = new CartDto.Request(product2.getProductId(), 5);

        cartService.addProduct("사용자1", request1);
        cartService.addProduct("사용자2", request2);
    }
}
