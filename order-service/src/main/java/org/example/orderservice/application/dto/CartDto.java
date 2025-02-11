package org.example.orderservice.application.dto;

import java.util.List;

public record CartDto(
        String userId,
        List<CartItemDto> items
) {
}
