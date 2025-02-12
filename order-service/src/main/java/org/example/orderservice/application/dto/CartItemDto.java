package org.example.orderservice.application.dto;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record CartItemDto(
        String name,
        Long productId,
        int quantity,
        BigDecimal price
) {
}
