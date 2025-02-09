package org.example.cartservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {
    private Long productId;
    private String skuCode;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
