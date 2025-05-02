package com.codewithmosh.store.payments;

import com.codewithmosh.store.orders.OrderStatus;
import lombok.Getter;

@Getter

public class PaymentResult {
    private Long orderId;
    private OrderStatus paymentStatus;

    public PaymentResult(Long aLong, OrderStatus orderStatus) {
        this.orderId = aLong;
        this.paymentStatus = orderStatus;
    }
}
