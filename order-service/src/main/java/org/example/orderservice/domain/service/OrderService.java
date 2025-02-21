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
import org.example.orderservice.domain.model.order.OrderPaidEvent;
import org.example.orderservice.domain.model.order.OrderStatus;
import org.example.orderservice.infrastructure.exception.ProductNotFoundException;
import org.example.orderservice.infrastructure.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartServiceClient cartServiceClient;
    private final ProductServiceClient productServiceClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        CartDto cartDto = cartServiceClient.getCart(orderRequest.userId());

        List<Long> productIds = cartDto.items().stream()
                .map(CartItemDto::productId)
                .toList();
        log.info(productIds.toString());

        List<ProductPriceResponse> productPrices = productServiceClient.getProductPrices(productIds);
        log.info(productPrices.toString());
        BigDecimal totalPrice = cartDto.items().stream()
                .map(item -> {
                    ProductPriceResponse priceResponse = productPrices.stream()
                            .filter(p -> p.id().equals(item.productId()))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException("Product with id: " + item.productId() + " not found"));

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

    @Transactional
    public void makePaid(UUID id) {
        Order order = getOrder(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order cannot be paid");
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Order is already paid");
        }
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        applicationEventPublisher.publishEvent(new OrderPaidEvent(this, order));
        log.info("Event published");
    }

    private Optional<Order> getOrder(UUID id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public void makeCancelled(UUID id) {
        Order order = getOrder(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Paid order cannot be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
