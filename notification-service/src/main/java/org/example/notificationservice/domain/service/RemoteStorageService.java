package org.example.notificationservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class RemoteStorageService {
    public InputStream fetch(String bucketPath) {
    }
}
