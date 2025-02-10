package org.example.cartservice.dto;

import java.math.BigDecimal;

public record CartItemDto(
        Long categoryId,
        Long productId,
        String skuCode,
        String name,
        Integer quantity,
        BigDecimal price
) {
}
