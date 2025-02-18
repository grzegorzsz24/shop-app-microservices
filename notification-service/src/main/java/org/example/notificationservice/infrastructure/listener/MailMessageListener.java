package org.example.notificationservice.infrastructure.listener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.domain.model.Mail;
import org.example.notificationservice.domain.service.MailSenderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class MailMessageListener {
    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    public static final int MAX_RETRIES_COUNT = 3;
    private final MailSenderService mailService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.rabbitmq.exchanges.mail}")
    private String mailExchange;

    @RabbitListener(queues = "${app.rabbitmq.queues.mail}")
    void processMessage(@Valid Mail mail) {
        mailService.sendMail(mail);
    }

    @RabbitListener(queues = "${app.rabbitmq.queues.mail-dead-letter}")
    void processFailedMessagesRetryHeaders(Message failedMessage) {
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties()
                .getHeaders().get(HEADER_X_RETRIES_COUNT);

        if (retriesCnt == null) retriesCnt = 1;
        if (retriesCnt > MAX_RETRIES_COUNT) {
            log.info("Discarding failed message with retries count {}", retriesCnt);
            return;
        }

        log.info("Retrying message for the {} time", retriesCnt);
        failedMessage.getMessageProperties()
                .getHeaders().put(HEADER_X_RETRIES_COUNT, ++retriesCnt);

        rabbitTemplate.send(mailExchange,
                failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
    }
}
