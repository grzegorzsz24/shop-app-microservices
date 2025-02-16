package org.example.orderservice.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Mail(
        @Email
        String recipient,
        @NotBlank
        String subject,
        @NotBlank
        String message,
        @NotBlank
        String contentType,
        String bucketPath
) {
}

