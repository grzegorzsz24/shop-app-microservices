package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.orderservice.application.client.CartServiceClient;
import org.example.orderservice.application.client.ProductServiceClient;
import org.example.orderservice.application.dto.CartDto;
import org.example.orderservice.application.dto.CartItemDto;
import org.example.orderservice.application.dto.ProductPriceResponse;
import org.example.orderservice.application.dto.order.OrderResponse;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartServiceClient cartServiceClient;
    private final ProductServiceClient productServiceClient;

    public OrderResponse placeOrder(String userId) {
        CartDto cartDto = cartServiceClient.getCart(userId);

        List<Long> productIds = cartDto.items().stream()
                .map(CartItemDto::id)
                .toList();

        List<ProductPriceResponse> productPrices = productServiceClient.getProductPrices(productIds);

        BigDecimal totalPrice = cartDto.items().stream()
                .map
    }
}
