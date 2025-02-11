package org.example.orderservice.infrastructure.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
class StripeApiConfig {

    @Value("${stripe.api-key}")
    private static String apiKey;

    @PostConstruct
    static void init() {
        Stripe.apiKey = apiKey;
    }
}
