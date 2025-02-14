package org.example.notificationservice.infrastructure.exception;

public class MessageBuildingException extends RuntimeException {
    public MessageBuildingException(final String message, Throwable cause) {
        super(message, cause);
    }
}
