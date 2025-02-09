package org.example.cartservice.dto;

import java.util.List;

public record CartResponse(
        String userId,
        List<CartItemDto> items
) {
}
