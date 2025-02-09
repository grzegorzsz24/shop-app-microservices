package org.example.cartservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.dto.CartItemDto;
import org.example.cartservice.exception.CartNotFound;
import org.example.cartservice.mapper.CartMapper;
import org.example.cartservice.model.Cart;
import org.example.cartservice.repository.CartRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RedisTemplate<String, Cart> redisTemplate;
    private final CartMapper cartMapper;

    public CartResponse getCart(String userId) {
        String key = "cart: " + userId;
        Cart cart = redisTemplate.opsForValue().get(key);
        if (cart == null) {
            throw new CartNotFound("Cart not found");
        }

        return cartMapper.toResponse(cart);
    }

    public CartResponse addToCart(String userId, CartItemDto item) {
        String key = "cart: " + userId;
        Cart cart = redisTemplate.opsForValue().get(key);
        if (cart == null) {
            cart = new Cart(userId);
        }

        cart.addItem(cartMapper.toItem(item));
        redisTemplate.opsForValue().set(key, cart, Duration.ofHours(24));
        cartRepository.save(cart);

        return cartMapper.toResponse(cart);
    }
}
