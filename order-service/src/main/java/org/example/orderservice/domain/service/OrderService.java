package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.orderservice.application.client.CartServiceClient;
import org.example.orderservice.application.client.ProductServiceClient;
import org.example.orderservice.application.dto.CartDto;
import org.example.orderservice.application.dto.CartItemDto;
import org.example.orderservice.application.dto.ProductPriceResponse;
import org.example.orderservice.application.dto.order.OrderRequest;
import org.example.orderservice.application.dto.order.OrderResponse;

import org.example.orderservice.domain.mapper.OrderMapper;
import org.example.orderservice.domain.model.order.Order;
import org.example.orderservice.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartServiceClient cartServiceClient;
    private final ProductServiceClient productServiceClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        CartDto cartDto = cartServiceClient.getCart(orderRequest.userId());

        List<Long> productIds = cartDto.items().stream()
                .map(CartItemDto::id)
                .toList();

        List<ProductPriceResponse> productPrices = productServiceClient.getProductPrices(productIds);

        BigDecimal totalPrice = cartDto.items().stream()
                .map(item -> {
                    ProductPriceResponse priceResponse = productPrices.stream()
                            .filter(p -> p.productId().equals(item.id()))
                            .findFirst()
                            .orElseThrow(RuntimeException::new);

                    return priceResponse.price().multiply(BigDecimal.valueOf(item.quantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .ordererEmail(orderRequest.ordererEmail())
                .price(totalPrice)
                .orderedProducts(cartDto.items())
                .build();

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
