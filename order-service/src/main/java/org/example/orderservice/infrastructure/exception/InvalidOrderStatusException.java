package org.example.orderservice.infrastructure.exception;

import org.example.orderservice.domain.model.order.OrderStatus;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(OrderStatus orderStatus) {
        super("Order is already " + orderStatus);
    }
}
