package org.example.notificationservice.domain.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.example.notificationservice.domain.model.Mail;
import org.example.notificationservice.infrastructure.exception.MessageBuildingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

@Component
public class MessageFactory {
    private final String senderName;
    private final String senderMail;
    private final AttachmentFactory attachmentFactory;

    MessageFactory(@Value("${app.mail.sender.name}") String senderName,
                   @Value("${app.mail.sender.mail}") String senderMail,
                   AttachmentFactory attachmentFactory) {
        this.senderName = senderName;
        this.senderMail = senderMail;
        this.attachmentFactory = attachmentFactory;
    }

    MimeMessage createMessage(Supplier<MimeMessage> messageSupplier, Mail mail) {
        try {
            MimeMessage message = messageSupplier.get();
            message.setFrom(new InternetAddress(senderMail, senderName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.recipient()));
            message.setSubject(mail.subject());
            message.setContent(builContent(mail));

            return message;
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MessageBuildingException(e.getMessage(), e.getCause());
        }
    }

    private Multipart builContent(Mail mail) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setContent(mail.message(), mail.contentType());
        messageBodyPart.setHeader("Content-type", mail.contentType());

        multipart.addBodyPart(messageBodyPart);

        attachmentFactory.addAttachmentsFromS3(mail.bucketPath(), multipart);

        return multipart;
    }
}
