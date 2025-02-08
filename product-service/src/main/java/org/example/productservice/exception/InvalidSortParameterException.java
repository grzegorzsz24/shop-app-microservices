package org.example.productservice.exception;

public class InvalidSortParameterException extends RuntimeException {
    public InvalidSortParameterException(String message) {
        super(message);
    }
}
