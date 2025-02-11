package org.example.orderservice.application.client;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.ProductPriceResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private final RestClient productRestClient;

    public List<ProductPriceResponse> getProductPrices(List<Long> productIds) {
        return productRestClient.post()
                .uri("/api/categories/products/prices")
                .body(productIds)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
