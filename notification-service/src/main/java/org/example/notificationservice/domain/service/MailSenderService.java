package org.example.notificationservice.domain.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.domain.model.Mail;
import org.example.notificationservice.infrastructure.exception.MailSendingException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;
    private final MessageFactory messageFactory;

    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = messageFactory.createMessage(mailSender::createMimeMessage, mail);
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendingException(e.getMessage(), e.getCause());
        }
    }
}
