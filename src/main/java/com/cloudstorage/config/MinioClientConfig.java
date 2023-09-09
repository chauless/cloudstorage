package com.cloudstorage.config;

import io.minio.MinioClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio.client")
@Setter
public class MinioClientConfig {

    private String endpoint;
    private String user;
    private String password;

    @Bean
    public MinioClient configuredMinioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(user, password)
                .build();
    }
}