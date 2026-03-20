package com.back._1cafe.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;

    public CartDto getCart(String guestId) {

        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseThrow(() -> new RuntimeException("장바구니가 없습니다.")); // 비어있을 시 500 에러.


        List<CartDto.CartItemDto> items = cart.getCartItemList().stream()
                .map(item -> new CartDto.CartItemDto(
                        item.getCartItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .toList();

        int totalAmount = items.stream()
                .mapToInt(CartDto.CartItemDto::itemTotal)
                .sum();

        return new CartDto(
                cart.getCartId(),
                cart.getGuestId(),
                items,
                totalAmount
        );
    }

    //Todo->상품쪽 개발 완료시 가능
//     private final ProductRepository productRepository;

    //장바구니에 상품 추가
    //Todo 상품 개발 완료시 진행
    /*@Transactional
    public CartDto addProduct(String guestId, CartDto.Request request) {
        // 1. 장바구니 찾기 (없으면 생성)
        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseGet(() -> cartRepository.save(new Cart(guestId)));
        // 2. 상품 존재 여부 확인
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 3. 장바구니에 이미 담긴 상품인지 확인
        cart.getCartItemList().stream()
                .filter(item -> item.getProduct().getProductId() == product.getProductId())
                .findFirst()
                .ifPresentOrElse(
                        // 이미 있다면 수량만 추가
                        item -> item.addQuantity(request.quantity()),
                        // 없다면 새로 생성하여 리스트에 추가
                        () -> cart.getCartItemList().add(new CartItem(cart, product, request.quantity()))
                );
        return convertToDto(cart);
    }*/

    //엔티티 및 DTO 매핑
    private CartDto convertToDto(Cart cart) {
        List<CartDto.CartItemDto> itemDtos = cart.getCartItemList().stream()
                .map(item -> new CartDto.CartItemDto(
                        item.getCartItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                )).toList();

        return new CartDto(
                cart.getCartId(),
                cart.getGuestId(),
                itemDtos,
                cart.getTotalAmount()
        );
    }
}
