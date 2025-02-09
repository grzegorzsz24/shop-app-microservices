package org.example.cartservice.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@RedisHash("Cart")
public class Cart {
    private String userId;
    private List<CartItem> items;

    public Cart(String userId) {
        this.userId = userId;
    }

    public void addItem(CartItem item) {
        this.items.add(item);
    }
}
