package org.example.orderservice.application.dto.order;

import java.util.List;

public record OrderRequest(
        String ordererEmail,

        List<OrderProductRequest> orderedProducts
) {
}
