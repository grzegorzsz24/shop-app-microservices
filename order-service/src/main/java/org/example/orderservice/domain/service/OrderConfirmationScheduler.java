package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.model.Mail;
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
    private final MessageSender messageSender;

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

        Mail mail = Mail.builder()
                .recipient(event.getOrder().getOrdererEmail())
                .subject("Order Confirmation")
                .message("Thank you for your order!")
                .contentType("application/pdf")
                .bucketPath(fileName)
                .build();
        messageSender.sendMailRequest(mail);
    }
}
