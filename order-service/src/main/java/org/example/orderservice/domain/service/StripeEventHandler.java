package org.example.orderservice.domain.service;

import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.domain.model.payment.Payment;
import org.example.orderservice.domain.model.payment.PaymentStatus;
import org.example.orderservice.infrastructure.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StripeEventHandler {
    private final StripeWebhookFacade stripeWebhookFacade;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public void handleEvent(Event event) {
        log.info("Handling payment event");
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSuccessful(event);
            case "checkout.session.completed" -> handlePaymentProcessing(event);
            case "checkout.session.async_payment_failed" -> handleAsyncPaymentFailed(event);
            default -> log.error("Unhandled event type: {}", event.getType());
        }
    }

    private void handleAsyncPaymentFailed(Event event) {
        changePaymentStatus(event, PaymentStatus.CANCELLED);
        orderService.makeCancelled(getOrderIdFromEvent(event));
    }

    private void handlePaymentProcessing(Event event) {
        changePaymentStatus(event, PaymentStatus.PROCESSING);
    }

    private void handlePaymentSuccessful(Event event) {
        changePaymentStatus(event, PaymentStatus.PAID);
        orderService.makePaid(getOrderIdFromEvent(event));
    }

    private UUID getOrderIdFromEvent(Event event) {
        return UUID.fromString(stripeWebhookFacade.extractOrderIdFromEvent(event));
    }

    private void changePaymentStatus(Event event, PaymentStatus status) {
        String sessionId = stripeWebhookFacade.extractSessionIdFromEvent(event);
        Payment payment = paymentRepository.findByPaymentSessionId(sessionId)
                .orElseThrow(() -> new NoSuchElementException("Payment session not found"));

        if (payment.getStatus() != PaymentStatus.PAID) {
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
    }
}
