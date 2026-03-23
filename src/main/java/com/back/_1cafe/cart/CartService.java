package com.back._1cafe.cart;

import com.back._1cafe.global.exception.customExcetpion.CartItemNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.ProductNotFoundException;
import com.back._1cafe.product.Product;
import com.back._1cafe.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartDto getCart(String guestId) {

        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지않습니다."));

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

    //장바구니에 상품 추가
    @Transactional
    public CartDto addProduct(String guestId, CartDto.Request request) {
        // 1. 장바구니 찾기 (없으면 생성)
        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseGet(() -> cartRepository.save(new Cart(guestId)));
        // 2. 상품 존재 여부 확인
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException("%d번 상품은 존재하지 않는 상품입니다.".formatted(request.productId())));

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
        cartRepository.flush();
        return convertToDto(cart);
    }

    public long count() {
        return cartRepository.count();
    }

    @Transactional
    public CartDto modifyProduct(String guestId, Integer productId, Integer quantity){
        //장바구니 찾기 없을시 예외발생
        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseThrow(()->new CartNotFoundException("장바구니가 존재하지않습니다."));
        //상품존재확인(없으면 예외발생)
        CartItem targetItem=cart.getCartItemList().stream()
                .filter(item->item.getProduct().getProductId()==productId)
                .findFirst()
                .orElseThrow(()->new CartItemNotFoundException("장바구니에 해당 상품이 존재하지 않습니다."));
        //갯수 수정
        targetItem.modifyQuantity(quantity);
        //Dto리턴
        return convertToDto(cart);
    }

    @Transactional
    public CartDto clearCart(String guestId) {
        Cart cart = cartRepository.findByGuestId(guestId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지않습니다."));

        // 장바구니 전체가 아닌 아이템만 제거.
        cart.getCartItemList().clear();

        return convertToDto(cart);
    }

    //삭제구현
    @Transactional
    public CartDto deleteProduct(String guestId, Integer productId) {
        Cart cart = cartRepository.findByGuestId(guestId).
                orElseThrow(()->new CartNotFoundException("장바구니가 존재하지않습니다."));
        CartItem targetItem=cart.getCartItemList().stream()
                .filter(item->item.getProduct().getProductId()==productId)
                .findFirst().orElseThrow(()->new CartItemNotFoundException("장바구니에 해당 상품이 존재하지 않습니다."));
        cart.getCartItemList().remove(targetItem);
        return convertToDto(cart);
    }

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
