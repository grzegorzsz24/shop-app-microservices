package org.example.orderservice.application.dto.order;

public record OrderRequest(
        String userId,
        String ordererEmail
) {
}
