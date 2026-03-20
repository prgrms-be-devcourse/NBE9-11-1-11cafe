package com.back._1cafe.product;


import com.back._1cafe.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public RsData<List<ProductDto>> list(){
         List<ProductDto> productDtoList = productService.getAllProducts().stream()
                                    .map(ProductDto::new)
                                    .toList();
         return new RsData<>(true, "상품 목록 조회 성공", productDtoList);
    }
}
