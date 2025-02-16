package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.model.order.OrderPaidEvent;
import org.example.orderservice.infrastructure.exception.StorageSaveException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OrderConfirmationScheduler {
    private final PdfGeneratorService pdfGeneratorService;
    private final RemoteStorageService remoteStorageService;

    @EventListener
    public void generateOrderConfirmation(OrderPaidEvent event) {
        String fileName = "order-summaries/" + LocalDate.now() + ".pdf";
        byte[] data = pdfGeneratorService.generateOrderSummary(event.getOrder());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(data);
        } catch (IOException e) {
            throw new StorageSaveException(e.getMessage(), e.getCause());
        }

        remoteStorageService.save(fileName, "application/pdf", outputStream);
    }
}
