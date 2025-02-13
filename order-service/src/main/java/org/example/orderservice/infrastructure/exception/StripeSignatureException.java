package org.example.orderservice.infrastructure.exception;

public class StripeSignatureException  extends RuntimeException {
    public StripeSignatureException(String message) {
        super(message);
    }
}
