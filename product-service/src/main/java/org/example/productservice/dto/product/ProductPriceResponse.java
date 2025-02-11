package org.example.productservice.dto.product;

import java.math.BigDecimal;

public record ProductPriceResponse(
        Long id,
        BigDecimal price
) {
}
