package org.example.productservice.dto.product;

import java.math.BigDecimal;

public record FilteredProductRequest(
        Long category,
        String name,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
