package org.example.notificationservice.infrastructure.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.domain.model.Mail;
import org.example.notificationservice.domain.service.MailSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MailMessageListener {
    private final MailSenderService mailService;
    private final RabbitTemplate rabbitTemplate;

    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    public static final int MAX_RETRIES_COUNT = 3;

    @Value("${app.rabbitmq.exchanges.mail}")
    private String mailExchange;

    @RabbitListener(queues = "${app.rabbitmq.queues.mail}")
    void processMessage(@Valid Mail mail) {
        mailService.sendMail(mail);
    }
}
