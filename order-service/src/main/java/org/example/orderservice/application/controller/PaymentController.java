package org.example.orderservice.application.controller;

import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.payment.PaymentLinkRequest;
import org.example.orderservice.application.dto.payment.PaymentLinkResponse;
import org.example.orderservice.domain.service.PaymentService;
import org.example.orderservice.domain.service.StripeEventHandler;
import org.example.orderservice.domain.service.StripeWebhookFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
class PaymentController {
    private final PaymentService paymentService;
    private final StripeWebhookFacade stripeWebhookFacade;
    private final StripeEventHandler stripeEventHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    PaymentLinkResponse createPaymentLink(@RequestBody PaymentLinkRequest request) {
        return paymentService.createPaymentLink(request);
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    void stripeWebhook(@RequestBody String json, HttpServletRequest request) {
        Event event = stripeWebhookFacade.createStripeEvent(json, request);
        stripeEventHandler.handleEvent(event);
    }
}
