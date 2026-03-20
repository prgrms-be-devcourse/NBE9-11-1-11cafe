package com.back._1cafe.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //전체 조회
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
