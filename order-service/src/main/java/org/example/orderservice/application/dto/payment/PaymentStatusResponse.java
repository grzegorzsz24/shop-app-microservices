package org.example.orderservice.application.dto.payment;

import org.example.orderservice.domain.model.payment.PaymentStatus;

public record PaymentStatusResponse(
        String paymentSessionId,
        PaymentStatus paymentStatus
) {
    @SuppressWarnings("unused")
    public PaymentStatusResponse(String paymentSessionId, String paymentStatus) {
        this(paymentSessionId, PaymentStatus.valueOf(paymentStatus));
    }
}
