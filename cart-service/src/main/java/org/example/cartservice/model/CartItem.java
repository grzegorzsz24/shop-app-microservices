package org.example.cartservice.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItem implements Serializable {
    private Long categoryId;
    private Long productId;
    private String skuCode;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
