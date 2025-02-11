package org.example.orderservice.infrastructure.exception;

public class ProductNotInStock extends RuntimeException {
    public ProductNotInStock(String message) {
        super(message);
    }
}
