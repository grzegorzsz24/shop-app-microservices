package org.example.orderservice.infrastructure.repository;

import org.example.orderservice.application.dto.payment.PaymentStatusResponse;
import org.example.orderservice.domain.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query(name = "Payment.findPaymentStatusResponseByOrderUUID", nativeQuery = true)
    Optional<PaymentStatusResponse> findPaymentStatusResponseByOrderUUID(UUID uuid);

    Optional<Payment> findByPaymentSessionId(String id);
}
