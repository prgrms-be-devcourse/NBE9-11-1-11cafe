package com.back._1cafe.order;

import com.back._1cafe.cart.Cart;
import com.back._1cafe.cart.CartItem;
import com.back._1cafe.cart.CartRepository;
import com.back._1cafe.customer.Customer;
import com.back._1cafe.customer.CustomerRepository;
import com.back._1cafe.global.exception.customExcetpion.CartItemNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
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

    @Transactional
    public Orders createOrder (String guestId,OrderRequestBody orderRequestBody) {

        // customer 객체 생성해서 저장
        // 이 부분 논의 필요 - customer에는 email만 남기는게 어떨지?
        Customer customer = customerRepository.findByEmail(orderRequestBody.email())
                .orElseGet(()->{
                    Customer newCustomer=new Customer(orderRequestBody.email(), orderRequestBody.address(), orderRequestBody.postcode());
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
        Orders orders = new Orders(customer);

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

        return orders;
    }

    // "우리는 매일 전날 오후 2시부터 당일 오후 2시까지의 주문을 모아서 처리합니다."
    public int setDeliveryBatch(LocalDateTime orderTime) {
        // 현재 시간에서 14시간을 뒤로 미는 방법
        // 예: 20일 13시 -> 19일 23시 (19일자 배치)
        // 예: 20일 15시 -> 20일 01시 (20일자 배치)
        LocalDateTime referenceTime = orderTime.minusHours(14);

        // 날짜 포맷팅 (20260320 형태)
        String deliveryBatch = referenceTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return Integer.parseInt(deliveryBatch);
    }
}



























/*
    public Order createOrder(OrderForm orderForm) {
        // 새로운 order를 만들기 (order를 만든다는게 무슨 뜻? 무슨 매개변수를 통해서 어떤 정보를 정확히 저장?)
        // totalAmount 가져오기
        // batchId 가져오기
        // orderItem 가져오기
    }
    List<OrderItem> orderItems = this.orderService.getOrderItems(order.getId());
    int totalAmount = this.orderService.totalAmount(orderItems);
    int batchId = this.orderService.batchId(order.getId());
    // product price랑 quantity를 전달...할 필요가 없고 그냥 리스트 그대로 전달해서
    // service가 하나하나 다 분리해서 계산하게 하면 되잖아..?

    // order list 가지고 와야할듯
    // total amount 메서드 부르고 order list에 담긴 product 매개변수로 전달
    // order entity의 local date time을 변수 새로 만들어서 저장
    // 그 변수를 batchid 계산하는 메서드에 매개변수로 전달

    public List<OrderItem> getOrderItems () {

    }

    public int totalAmount () {

    }

    public int batchId () {

    }*/