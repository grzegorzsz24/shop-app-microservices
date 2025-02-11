package org.example.orderservice.application.dto.order;

import org.example.orderservice.application.dto.CartItemDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String ordererEmail,
        LocalDateTime orderDate,
        BigDecimal price,
        Set<CartItemDto> orderedProducts
) {
}
