package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class RemoteStorageService {
    private final S3Client s3Client;
}
