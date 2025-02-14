package org.example.notificationservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.infrastructure.exception.StorageFetchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class RemoteStorageService {
    private final S3Client s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public InputStream fetch(String bucketPath) throws StorageFetchException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(bucketPath)
                .build();

        try {
            ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
            return new ByteArrayInputStream(responseBytes.asByteArray());
        } catch (S3Exception e) {
            throw new StorageFetchException(e.getMessage(), e.getCause());
        }
    }
}
