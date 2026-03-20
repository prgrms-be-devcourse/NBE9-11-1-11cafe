package com.back._1cafe.product;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createAt;
}
