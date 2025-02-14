package org.example.notificationservice.domain.service;

import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachmentFactory {
    public void addAttachmentsFromS3(String s, Multipart multipart) {
    }
}
