package com.back._1cafe.global.initData;

import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class BaseInitData implements ApplicationRunner {

    private final ProductRepository productRepository;

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
    }
}
