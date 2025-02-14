package org.example.notificationservice.domain.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.domain.model.Mail;
import org.example.notificationservice.infrastructure.exception.MailSendingException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {
    private final JavaMailSender mailSender;
    private final MessageFactory messageFactory;

    @Retryable(
            retryFor = {MailSendingException.class},
            backoff = @Backoff(delayExpression = "${spring.mail.retry.backoff}")
    )
    public void sendMail(Mail mail) {
        log.info("Preparing to send email to recipient: ...");
        try {
            MimeMessage mimeMessage = messageFactory.createMessage(mailSender::createMimeMessage, mail);
            mailSender.send(mimeMessage);

            log.info("Successfully sent email to recipient");
        } catch (MailException e) {
            throw new MailSendingException(e.getMessage(), e.getCause());
        }
    }

    @SuppressWarnings("unused")
    @Recover
    private void recover(MailSendingException e, Mail mail) {
        log.error("Failed to send email to recipient: ... after retries", e);
    }
}
