package org.example.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.infrastructure.exception.StorageSaveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteStorageService implements StorageService {

    private final S3Client s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.bucket.base.dir}")
    private String bucketBaseDir;

    @Override
    public String save(String fileName, String contentType, ByteArrayOutputStream data, Path directory) {
        String key = bucketBaseDir + directory.toString() + "/" + fileName;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data.toByteArray()));
            log.info("saved {} to {}", fileName, directory);
        } catch (S3Exception e) {
            throw new StorageSaveException(e.getMessage(), e.getCause());
        }

        return key;
    }
}
