package com.back._1cafe.product;

import java.time.LocalDateTime;

public record ProductDto(
    Integer productId,
    String productName,
    String description,
    int price,
    LocalDateTime createdAt
) {
    public ProductDto (Product product){
        this(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreateAt()
        );
    }
}
