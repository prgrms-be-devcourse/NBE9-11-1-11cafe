package com.back._1cafe.order;

import com.back._1cafe.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 10)
    private String postcode;


    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int deliveryBatch;

    private int totalPrice;

    // 고객 정보 받아서 새로운 주문하는 생성자
    public Orders(Customer customer,String address, String postcode){
        this.customer=customer;
        this.address=address;
        this.postcode=postcode;
    }
    //주문상품을 추가하고 연관관계 맺는 메서드
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }
    //배치를 할당하는 메서드
    public void assignDeliveryBatch(int deliveryBatch) {
        this.deliveryBatch = deliveryBatch;
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.orderItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
