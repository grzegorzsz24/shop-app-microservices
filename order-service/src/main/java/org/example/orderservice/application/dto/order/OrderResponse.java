package org.example.orderservice.application.dto.order;

import java.math.BigDecimal;

public record OrderResponse(
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
}
