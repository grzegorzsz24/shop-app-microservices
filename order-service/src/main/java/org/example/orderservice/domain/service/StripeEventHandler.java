package org.example.orderservice.domain.service;

import com.stripe.model.Event;
import org.springframework.stereotype.Component;

@Component
public class StripeEventHandler {
    public void handleEvent(Event event) {
    }
}
