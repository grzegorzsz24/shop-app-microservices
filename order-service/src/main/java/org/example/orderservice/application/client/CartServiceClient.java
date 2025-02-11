package org.example.orderservice.application.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.application.dto.CartDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartServiceClient {
    private final RestClient cartRestClient;

    public CartDto getCart(String userId) {
        return cartRestClient.get()
                .uri("/api/cart")
                .header("X-User-Id", userId)
                .retrieve()
                .body(CartDto.class);
    }
}
