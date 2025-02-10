package org.example.cartservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartservice.client.ProductClient;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.dto.CartItemDto;
import org.example.cartservice.exception.CartNotFound;
import org.example.cartservice.exception.ProductNotInStock;
import org.example.cartservice.mapper.CartMapper;
import org.example.cartservice.model.Cart;
import org.example.cartservice.repository.CartRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final RedisTemplate<String, Cart> redisTemplate;
    private final CartMapper cartMapper;
    private final ProductClient productClient;

    public CartResponse getCart(String userId) {
        String key = "cart: " + userId;
        Cart cart = redisTemplate.opsForValue().get(key);
        log.info("cart: {}", cart);
        if (cart == null) {
            cart = cartRepository.findById(userId)
                    .orElseThrow(() -> new CartNotFound("Cart not found"));
        }

        return cartMapper.toResponse(cart);
    }

    public CartResponse addToCart(String userId, CartItemDto item) {
        var isProductInStock = productClient.isProductAvailable(item.categoryId(), item.productId(), item.quantity());
        if (isProductInStock) {
            String key = "cart: " + userId;
            Cart cart = redisTemplate.opsForValue().get(key);
            if (cart == null) {
                cart = cartRepository.findById(userId).orElse(new Cart(userId));
            }

            cart.addItem(cartMapper.toItem(item));
            redisTemplate.opsForValue().set(key, cart, Duration.ofHours(24));
            cartRepository.save(cart);

            return cartMapper.toResponse(cart);
        } else {
            throw new ProductNotInStock("Product with skuCode " + item.skuCode() + " is not in stock");
        }
    }
}
