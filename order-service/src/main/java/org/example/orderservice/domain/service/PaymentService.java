package org.example.orderservice.domain.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.CartItemDto;
import org.example.orderservice.application.dto.payment.PaymentLinkRequest;
import org.example.orderservice.application.dto.payment.PaymentLinkResponse;
import org.example.orderservice.application.dto.payment.PaymentStatusResponse;
import org.example.orderservice.domain.model.order.Order;
import org.example.orderservice.domain.model.order.OrderStatus;
import org.example.orderservice.domain.model.payment.Payment;
import org.example.orderservice.domain.model.payment.PaymentStatus;
import org.example.orderservice.domain.model.payment.PersistentMoney;
import org.example.orderservice.infrastructure.exception.CustomStripeException;
import org.example.orderservice.infrastructure.exception.InvalidOrderStatusException;
import org.example.orderservice.infrastructure.exception.ProcessingPaymentException;
import org.example.orderservice.infrastructure.repository.OrderRepository;
import org.example.orderservice.infrastructure.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public PaymentLinkResponse createPaymentLink(PaymentLinkRequest request) {
        Order order = Optional.of(request.orderId()).map(UUID::fromString).flatMap(orderRepository::findById)
                .orElseThrow(() -> new NoSuchElementException("Order does not exists"));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        Optional<PaymentStatusResponse> optionalResponse = paymentRepository.findPaymentStatusResponseByOrderUUID(order.getId());

        if (optionalResponse.isPresent()) {
            PaymentStatusResponse paymentStatusResponse = optionalResponse.get();

            if (paymentStatusResponse.paymentStatus() == PaymentStatus.CREATED) {
                return retrievePaymentLinkFromSession(paymentStatusResponse.paymentSessionId());
            }

            if (paymentStatusResponse.paymentStatus() == PaymentStatus.PROCESSING) {
                throw new ProcessingPaymentException("A payment session is already processing");
            }
        }

        SessionCreateParams.Builder params = createCheckoutSessionBuilder(request.orderId());

        addPaymentMethod("USD", params);

        PersistentMoney money = addLineItems(order, params);

        Map<String, String> paymentData = createNewSessionAndPaymentLink(params);

        Payment payment = Payment.builder()
                .paymentSessionId(paymentData.get("sessionId"))
                .status(PaymentStatus.CREATED)
                .order(order)
                .persistentMoney(money)
                .build();
        paymentRepository.save(payment);

        return new PaymentLinkResponse(paymentData.get("paymentUrl"));
    }

    private Map<String, String> createNewSessionAndPaymentLink(SessionCreateParams.Builder params) {
        try {
            Session session = Session.create(params.build());
            Map<String, String> result = new HashMap<>();
            result.put("sessionId", session.getId());
            result.put("paymentUrl", session.getUrl());
            return result;
        } catch (StripeException e) {
            throw new CustomStripeException("Stripe was unable to create a payment session");
        }
    }

    private PersistentMoney addLineItems(Order order, SessionCreateParams.Builder params) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        String currency = "USD";

        for (CartItemDto cartItem : order.getOrderedProducts()) {
            totalAmount = totalAmount.add(cartItem.price());

            SessionCreateParams.LineItem item = createLineItem(cartItem.name(), cartItem.quantity(),
                    currency, cartItem.price());

            params.addLineItem(item);
        }

        return new PersistentMoney(totalAmount, currency);
    }

    private SessionCreateParams.LineItem createLineItem(String name, int quantity, String currency, BigDecimal price) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) 1)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(price.multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(quantity + " " + name).build()
                                ).build()
                ).build();
    }

    private void addPaymentMethod(String currency, SessionCreateParams.Builder params) {
        params.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);

        switch (currency) {
            case "EUR":
                params.addPaymentMethodType(SessionCreateParams.PaymentMethodType.SEPA_DEBIT);
                break;
            case "PLN":
                params.addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK);
                break;
            default:
                break;
        }
    }

    private SessionCreateParams.Builder createCheckoutSessionBuilder(String orderId) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", orderId);

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl)
                .setCancelUrl(baseUrl + "/api/payments/cancel")
                .putAllMetadata(metadata)
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putAllMetadata(metadata)
                                .build()
                );
    }

    private PaymentLinkResponse retrievePaymentLinkFromSession(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return new PaymentLinkResponse(session.getUrl());
        } catch (StripeException ex) {
            throw new CustomStripeException("Stripe was unable to retrieve the payment link");
        }
    }
}
