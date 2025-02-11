package org.example.orderservice.domain.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Builder;
import org.example.orderservice.domain.model.payment.PersistentMoney;

@Builder
@Embeddable
public record OrderProduct(

        @Column(nullable = false)
        Long productId,

        @Column(nullable = false)
        String name,

        @Column(nullable = false)
        int quantity,

        @Embedded
        PersistentMoney price
) {
    public OrderProduct() {
        this(null, null, 0,
                new PersistentMoney(null, null));
    }
}
