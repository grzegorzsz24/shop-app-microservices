package org.example.orderservice.application.dto;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        int quantity,
        BigDecimal price
) {
}
