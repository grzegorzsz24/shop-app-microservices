package org.example.orderservice.application.dto;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record CartItemDto(
        Long id,
        int quantity,
        BigDecimal price
) {
}
