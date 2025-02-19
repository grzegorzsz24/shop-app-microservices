package org.example.cartservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cartservice.dto.CartItemDto;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
class CartController {
    private final CartService cartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    CartResponse getCart(HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        return cartService.getCart(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CartResponse addToCart(HttpServletRequest request, @RequestBody CartItemDto item) {
        String userId = request.getHeader("X-User-Id");
        return cartService.addToCart(userId, item);
    }
}
