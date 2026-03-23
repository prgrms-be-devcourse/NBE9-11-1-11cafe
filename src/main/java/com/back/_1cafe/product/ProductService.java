package com.back._1cafe.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //전체 조회
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    //상세보기
    public Optional<Product> getProduct(int postId){
        return productRepository.findById(postId);
    }
}
