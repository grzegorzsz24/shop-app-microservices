package org.example.notificationservice.infrastructure.exception;

public class MailSendingException extends RuntimeException {
    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
