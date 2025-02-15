package org.example.orderservice.domain.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.infrastructure.exception.CustomStripeException;
import org.example.orderservice.infrastructure.exception.StripeSignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class StripeWebhookFacade {

    @Value("${payment.stripe.webhook-secret-key}")
    private String endpointSecret;

    public Event createStripeEvent(String json, HttpServletRequest request) {
        String header = request.getHeader("Stripe-Signature");
        try {
            return Webhook.constructEvent(json, header, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new StripeSignatureException("Invalid Stripe signature");
        }
    }

    public String extractSessionIdFromEvent(Event event) {
        try {
            Session session;
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = extractPaymentIntentFromEvent(event);
                    session = extractSessionFromPaymentIntent(paymentIntent);
                    break;
                case "checkout.session.completed", "checkout.session.async_payment_failed":
                    session = extractSessionFromEvent(event);
                    break;
                default:
                    throw new CustomStripeException("Invalid event type");
            }
            return session.getId();
        } catch (StripeException | NoSuchElementException e) {
            log.error("Something went wrong while extracting sessionId from event.");
            throw new CustomStripeException("Session ID not found");
        }
    }

    private Session extractSessionFromEvent(Event event) {
        return (Session) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> {
                    log.error("Failed to deserialize Session from event: {}", event.getId());
                    return new NoSuchElementException("Could not extract session");
                });
    }

    private Session extractSessionFromPaymentIntent(PaymentIntent paymentIntent) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("payment_intent", paymentIntent.getId());

        List<Session> sessions = Session.list(params).getData();

        if (sessions.isEmpty()) throw new CustomStripeException("Session is empty");
        return sessions.getFirst();
    }

    private PaymentIntent extractPaymentIntentFromEvent(Event event) {
        return (PaymentIntent) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> {
                    log.error("Failed to deserialize PaymentIntent from event: {}", event.getId());
                    return new NoSuchElementException("Could not extract payment intent");
                });
    }

    public String extractOrderIdFromEvent(Event event) {
        return switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = extractPaymentIntentFromEvent(event);
                yield paymentIntent.getMetadata().get("orderId");
            }
            case "checkout.session.completed", "checkout.session.async_payment_failed" -> {
                Session session = extractSessionFromEvent(event);
                yield session.getMetadata().get("orderId");
            }
            default -> throw new CustomStripeException("Invalid event type");
        };
    }
}
