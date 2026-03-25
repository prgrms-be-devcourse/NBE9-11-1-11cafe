package com.back._1cafe.order;

import com.back._1cafe.cart.Cart;
import com.back._1cafe.cart.CartItem;
import com.back._1cafe.cart.CartRepository;
import com.back._1cafe.cart.CartService;
import com.back._1cafe.customer.Customer;
import com.back._1cafe.customer.CustomerRepository;
import com.back._1cafe.global.exception.customExcetpion.CartItemNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    @Operation(summary = "주문하기")
    public Orders createOrder (String guestId,OrderRequestBody orderRequestBody) {

        // customer 객체 생성해서 저장
        Customer customer = customerRepository.findByEmail(orderRequestBody.email())
                .orElseGet(()->{
                    Customer newCustomer=new Customer(orderRequestBody.email());
                    return customerRepository.save(newCustomer);
                });

        // Cart 세팅 및 예외처리
        Cart cart =cartRepository.findByGuestId(guestId)
                .orElseThrow(()->new CartNotFoundException("장바구니가 존재하지않습니다."));
        List<CartItem> cartItems = cart.getCartItemList();
        if(cartItems.isEmpty()){
            throw new CartItemNotFoundException("장바구니가 비어있습니다.");
        }

        // 주문 생성 및 연결
        Orders orders = new Orders(
                customer,
                orderRequestBody.address(),
                orderRequestBody.postcode()
        );


        for(CartItem cartItem:cartItems){
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
            orders.addOrderItem(orderItem);
        }

        // Batch 계산
        int deliveryBatch = setDeliveryBatch(orders.getCreatedAt());
        orders.assignDeliveryBatch(deliveryBatch);

        // Total Price 계산
        orders.calculateTotalPrice();

        // return orders
        orderRepository.save(orders);
        cartService.clearCart(guestId);
        return orders;
    }

    // "우리는 매일 전날 오후 2시부터 당일 오후 2시까지의 주문을 모아서 처리합니다."
    public int setDeliveryBatch(LocalDateTime orderTime) {
        // if orderTime의 시간이 14시 이전이면 deliveryBatch = 오늘 날짜를 포맷에 맞게 리턴
        // if orderTime의 시간이 14시 이후라면 orderTime의 yyyymmdd를 가져와서 하루 추가한다음에 deliveryBatch로 리턴
        LocalDateTime targetTime = (orderTime.getHour() < 14) ? orderTime : orderTime.plusDays(1);

        return Integer.parseInt(targetTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}