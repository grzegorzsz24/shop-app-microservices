package org.example.orderservice.infrastructure.exception;

public class StorageSaveException extends RuntimeException {
    public StorageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
