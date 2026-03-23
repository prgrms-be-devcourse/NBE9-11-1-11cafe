package com.back._1cafe.global.initData;

import com.back._1cafe.cart.CartDto;
import com.back._1cafe.cart.CartService;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitData implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final CartService cartService;

    @Lazy
    private final BaseInitData self;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("initData 호출 됨.");
        self.work1();
    }

    @Transactional
    public void work1() {

        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product("Columbia", 15000, "SINGLE_ORIGIN"),
                    new Product("Ethiopia", 17000, "SINGLE_ORIGIN"),
                    new Product("Brazil", 13000, "BLENDED"),
                    new Product("Kenya", 16000, "SINGLE_ORIGIN")
            ));
        }

        if (cartService.count() > 0) {
            return;
        }

        List<Product> products = productRepository.findAll();

        if (products.size() < 2) {
            throw new IllegalStateException("상품 데이터 부족");
        }

        CartDto.Request request1 = new CartDto.Request(products.get(0).getProductId(), 3);
        CartDto.Request request2 = new CartDto.Request(products.get(1).getProductId(), 5);

        cartService.addProduct("사용자1", request1);
        cartService.addProduct("사용자2", request2);
    }
}
