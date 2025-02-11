package org.example.orderservice.application.dto;

import java.math.BigDecimal;

public record OrderRequest(
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
}
