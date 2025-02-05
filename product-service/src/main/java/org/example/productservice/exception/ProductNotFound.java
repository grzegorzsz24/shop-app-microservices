package org.example.productservice.exception;

public class ProductNotFound extends RuntimeException {
    public ProductNotFound(String message) {
        super(message);
    }
}
