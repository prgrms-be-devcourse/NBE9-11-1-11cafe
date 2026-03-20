package com.back._1cafe.cart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @Column(nullable = false, unique = true)
    private String guestId;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
            , orphanRemoval = true)
    private List<CartItem> cartItemList = new ArrayList<>();

    //생성자
    public Cart(String guestId){
        this.guestId=guestId;
    }
    public int getTotalAmount() {
        return cartItemList.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

}
