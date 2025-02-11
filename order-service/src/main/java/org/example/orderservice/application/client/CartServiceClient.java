package org.example.orderservice.application.client;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.CartDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CartServiceClient {
    private final RestClient restClient;

    public CartDto getCart(String userId) {
        return restClient.get()
                .uri("/api/cart")
                .header("X-User-Id", userId)
                .retrieve()
                .body(CartDto.class);
    }
}
