package org.example.orderservice.application.dto;

import java.util.Set;

public record CartDto(
        String userId,
        Set<CartItemDto> items
) {
}
