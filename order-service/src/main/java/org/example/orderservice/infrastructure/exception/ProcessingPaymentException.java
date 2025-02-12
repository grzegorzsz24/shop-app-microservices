package org.example.orderservice.infrastructure.exception;

public class ProcessingPaymentException extends RuntimeException {
    public ProcessingPaymentException(String message) {
        super(message);
    }
}
