package com.cloudstorage.service;

import com.cloudstorage.config.MinioBucketConfig;
import com.cloudstorage.dto.FileUploadRequest;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.cloudstorage.utils.MinioRootFolderUtils.getUserRootFolderPrefix;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final MinioBucketConfig minioBucketConfig;

    public void uploadFile(FileUploadRequest fileUploadRequest) {
        MultipartFile file = fileUploadRequest.getFile();
        try (InputStream stream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(stream, file.getSize(), -1)
                    .bucket(minioBucketConfig.getBucketName())
                    .object(getUserRootFolderPrefix(fileUploadRequest.getOwner()) + file.getOriginalFilename())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
