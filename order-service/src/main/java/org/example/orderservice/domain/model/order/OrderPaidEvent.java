package org.example.orderservice.domain.model.order;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPaidEvent extends ApplicationEvent {
    private final Order order;

    public OrderPaidEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
