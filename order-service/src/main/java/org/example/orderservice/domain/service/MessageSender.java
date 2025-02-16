package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.model.Mail;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {

    @Value("${app.rabbitmq.exchanges.mails}")
    private String exchaneName;

    @Value("${app.rabbitmq.routing-keys.mails}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private static final int PRIORITY = 1;

    public void sendMailRequest(Mail mail) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setPriority(PRIORITY);

        Message message = rabbitTemplate.getMessageConverter().toMessage(mail, messageProperties);

        rabbitTemplate.convertAndSend(exchaneName, routingKey, message);
    }
}
