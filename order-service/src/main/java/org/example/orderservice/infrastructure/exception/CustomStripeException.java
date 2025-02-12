package org.example.orderservice.infrastructure.exception;

public class CustomStripeException extends RuntimeException {
    public CustomStripeException(String message) {
        super(message);
    }
}
