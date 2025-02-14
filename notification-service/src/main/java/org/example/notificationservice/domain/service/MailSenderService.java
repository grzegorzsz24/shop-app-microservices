package org.example.notificationservice.domain.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.domain.model.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;

    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
        }
    }
}
