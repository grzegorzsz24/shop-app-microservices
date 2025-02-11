package org.example.orderservice.dto;

public record PaymentStatusResponse(
        String paymentSessionId,
        PaymentStatus paymentStatus
) {
    public PaymentStatusResponse(String paymentSessionId, String paymentStatus) {
        this(paymentSessionId, PaymentStatus.valueOf(paymentStatus));
    }
}
