package com.cloudstorage.config;

import com.cloudstorage.service.BucketService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@Setter
@Getter
public class MinioBucketConfig {

    private final BucketService bucketService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @EventListener(ContextRefreshedEvent.class)
    public void createBucket() {
        try {
            if (!bucketService.isBucketExists(bucketName)) {
                bucketService.createBucket(bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Bucket '" + bucketName + "' is not created", e);
        }
    }
}
