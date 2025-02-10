package org.example.cartservice.exception;

public class ProductNotInStock extends RuntimeException {
    public ProductNotInStock(String message) {
        super(message);
    }
}
