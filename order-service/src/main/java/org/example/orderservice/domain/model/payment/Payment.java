package org.example.orderservice.domain.model.payment;

import jakarta.persistence.*;
import lombok.*;
import org.example.orderservice.application.dto.payment.PaymentStatusResponse;
import org.example.orderservice.domain.model.order.Order;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Payments")
@SqlResultSetMapping(
        name = "PaymentStatusResponseMapping",
        classes = @ConstructorResult(
                targetClass = PaymentStatusResponse.class,
                columns = {
                        @ColumnResult(name = "paymentSessionId", type = String.class),
                        @ColumnResult(name = "paymentStatus", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "Payment.findPaymentStatusResponseByOrderUUID",
        query = """
                SELECT payment_session_id AS paymentSessionId, p.status AS paymentStatus FROM payments p 
                JOIN orders o ON p.order_id = o.id
                WHERE p.order_id = :uuid AND (p.status = 'CREATED' OR p.status = 'PROCESSING')
                """,
        resultClass = PaymentStatusResponse.class,
        resultSetMapping = "PaymentStatusResponseMapping"
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    private String paymentSessionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    @Embedded
    private PersistentMoney persistentMoney;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

}
