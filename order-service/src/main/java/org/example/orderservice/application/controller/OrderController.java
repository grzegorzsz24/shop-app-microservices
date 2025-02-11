package org.example.orderservice.application.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.OrderRequest;
import org.example.orderservice.application.dto.OrderResponse;
import org.example.orderservice.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }
}
