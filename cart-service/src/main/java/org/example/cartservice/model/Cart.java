package org.example.cartservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Cart implements Serializable {

    @Serial
    private static final long serialVersionUID = 4849670465843396628L;

    private String userId;
    private List<CartItem> items = new ArrayList<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public void addItem(CartItem item) {
        this.items.add(item);
    }
}
