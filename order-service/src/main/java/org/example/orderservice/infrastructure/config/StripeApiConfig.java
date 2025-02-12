package org.example.orderservice.infrastructure.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
class StripeApiConfig {

    @Value("${payment.stripe.api-key}")
    private String apiKey;

    @PostConstruct
    void init() {
        Stripe.apiKey = this.apiKey;
    }
}
