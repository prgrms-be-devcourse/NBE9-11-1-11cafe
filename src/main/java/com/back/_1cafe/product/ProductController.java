package com.back._1cafe.product;


import com.back._1cafe.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "ProductController", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public RsData<List<ProductDto>> list(){
         List<ProductDto> productDtoList = productService.getAllProducts().stream()
                                    .map(ProductDto::new)
                                    .toList();
         return new RsData<>(true, "상품 목록 조회 성공", productDtoList);
    }

    @GetMapping("/{id}")
    public RsData<ProductDto> detail(@PathVariable("id") int productId){
        Product product = productService.getProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id: " + productId));
        return new RsData<>(true, "상품 조회 성공", new ProductDto(product));
    }
}
