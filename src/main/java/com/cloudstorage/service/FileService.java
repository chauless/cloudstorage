package com.cloudstorage.service;

import com.cloudstorage.config.MinioBucketConfig;
import com.cloudstorage.dto.FileDeleteRequest;
import com.cloudstorage.dto.FileUploadRequest;
import com.cloudstorage.dto.MinioObjectDto;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.cloudstorage.utils.MinioRootFolderUtils.getUserRootFolderPrefix;
import static com.cloudstorage.utils.MinioRootFolderUtils.removeUserRootFolderPrefix;

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

    public void deleteFile(FileDeleteRequest fileDeleteRequest) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioBucketConfig.getBucketName())
                    .object(getUserRootFolderPrefix(fileDeleteRequest.getOwner()) + fileDeleteRequest.getPath())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<MinioObjectDto> getUserFiles(String username, String folder, boolean isRecursive) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(minioBucketConfig.getBucketName())
                .prefix(getUserRootFolderPrefix(username) + folder)
                .recursive(isRecursive)
                .build());

        List<MinioObjectDto> files = new ArrayList<>();

        results.forEach(result -> {
            try {
                Item item = result.get();
                MinioObjectDto object = new MinioObjectDto(
                        username,
                        item.isDir(),
                        removeUserRootFolderPrefix(item.objectName(), username),
                        getFileNameFromPath(item.objectName())
                );
                files.add(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return files;
    }

    private static String getFileNameFromPath(String path) {
        if (!path.contains("/")) {
            return path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.lastIndexOf('/') > 0) {
            return path.substring(path.lastIndexOf('/') + 1);
        }
        return path;
    }

    public List<MinioObjectDto> getUserFiles(String username, String folder) {
        return getUserFiles(username, folder, false);
    }
}
