package org.example.orderservice.application.client;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.ProductPriceResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestClient restClient;

    public List<ProductPriceResponse> getProductPrices(List<Long> productIds) {
        return Collections.singletonList(restClient.post()
                .uri("/api/products/prices")
                .body(productIds)
                .retrieve()
                .body(ProductPriceResponse.class));
    }
}
