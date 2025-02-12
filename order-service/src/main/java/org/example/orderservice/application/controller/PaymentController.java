package org.example.orderservice.application.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.application.dto.payment.PaymentLinkRequest;
import org.example.orderservice.application.dto.payment.PaymentLinkResponse;
import org.example.orderservice.domain.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    PaymentLinkResponse createPaymentLink(@RequestBody PaymentLinkRequest request) {
        return paymentService.createPaymentLink(request);
    }
}
