package com.back._1cafe.order;

import com.back._1cafe.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter

@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    private int quantity;
    public OrderItem(Product product, int quantity){
        this.product=product;
        this.quantity=quantity;
    }
    public void assignOrder(Orders orders){
        this.orders=orders;
    }
}
