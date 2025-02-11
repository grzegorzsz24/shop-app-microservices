package org.example.orderservice.domain.model.order;

import jakarta.persistence.*;
import lombok.*;
import org.example.orderservice.domain.model.payment.Payment;
import org.example.orderservice.domain.model.payment.PersistentMoney;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String ordererEmail;

    @Column(nullable = false)
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Embedded
    private PersistentMoney price;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<OrderProduct> orderedProducts = new HashSet<>();
}
