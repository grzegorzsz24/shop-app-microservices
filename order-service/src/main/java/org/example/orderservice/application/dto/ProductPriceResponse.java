package org.example.orderservice.application.dto;

import java.math.BigDecimal;

public record ProductPriceResponse(
        Long id,
        BigDecimal price
) {
}
