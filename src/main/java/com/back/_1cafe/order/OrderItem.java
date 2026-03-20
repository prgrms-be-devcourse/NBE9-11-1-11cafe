package com.back._1cafe.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;
}