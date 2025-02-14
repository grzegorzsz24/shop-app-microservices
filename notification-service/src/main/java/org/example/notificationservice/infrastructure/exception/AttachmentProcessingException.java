package org.example.notificationservice.infrastructure.exception;

public class AttachmentProcessingException extends RuntimeException {
    public AttachmentProcessingException(final String message, Throwable cause) {
        super(message, cause);
    }
}
