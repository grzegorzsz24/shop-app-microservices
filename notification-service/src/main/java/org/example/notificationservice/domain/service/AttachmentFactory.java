package org.example.notificationservice.domain.service;

import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.infrastructure.exception.AttachmentProcessingException;
import org.example.notificationservice.infrastructure.exception.StorageFetchException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttachmentFactory {
    private final RemoteStorageService remoteStorageService;

    public void addAttachmentsFromS3(String bucketPath, Multipart multipart) {
        if (bucketPath == null || bucketPath.isEmpty()) {
            return;
        }

        log.info("Adding attachments from s3 bucket");

        try {
            multipart.addBodyPart(createAttachmentPart(bucketPath));
            log.info("Successfully added attachment from s3 bucket");
        } catch (MessagingException | IOException | StorageFetchException e) {
            throw new AttachmentProcessingException(e.getMessage(), e.getCause());
        }
    }

    private BodyPart createAttachmentPart(String bucketPath) throws MessagingException, IOException, StorageFetchException {
        InputStream attachmentStream = remoteStorageService.fetch(bucketPath);
        ByteArrayDataSource dataSource = new ByteArrayDataSource(attachmentStream, determineMimeType(bucketPath));
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(dataSource));
        bodyPart.setFileName(extractFileName(bucketPath));

        return bodyPart;
    }

    private String extractFileName(String bucketPath) {
        return bucketPath.substring(bucketPath.lastIndexOf("/") + 1);
    }

    private String determineMimeType(String bucketPath) {
        return switch (bucketPath.substring(bucketPath.lastIndexOf('.'))) {
            case ".pdf" -> "application/pdf";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            default -> "application/octet-stream";
        };
    }
}
