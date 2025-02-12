package org.example.orderservice.domain.service;

import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class StripeWebhookFacade {
    public Event createStripeEvent(String json, HttpServletRequest request) {
    }
}
