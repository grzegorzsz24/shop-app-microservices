package org.example.orderservice.application.dto.order;

public record OrderProductRequest(
        Long productId,
        String name,
        int quantity
) {
}
