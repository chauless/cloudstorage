package com.cloudstorage.service;

import com.cloudstorage.config.MinioBucketConfig;
import com.cloudstorage.dto.FolderDeleteRequest;
import com.cloudstorage.dto.FolderUploadRequest;
import com.cloudstorage.dto.MinioObjectDto;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cloudstorage.utils.MinioRootFolderUtils.getUserRootFolderPrefix;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FileService fileService;
    private final MinioClient minioClient;
    private final MinioBucketConfig minioBucketConfig;

    public void uploadFolder(FolderUploadRequest folderUploadRequest) {
        List<MultipartFile> files = folderUploadRequest.getFiles();
        String owner = folderUploadRequest.getOwner();
        try {
            List<SnowballObject> objects = convertToUploadObjects(files, owner);

            minioClient.uploadSnowballObjects(UploadSnowballObjectsArgs.builder()
                    .bucket(minioBucketConfig.getBucketName())
                    .objects(objects)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFolder(FolderDeleteRequest folderDeleteRequest) {
        List<MinioObjectDto> files = fileService.getAllUserFiles(folderDeleteRequest.getOwner(), folderDeleteRequest.getPath());

        List<DeleteObject> objects = convertToDeleteObjects(files);

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(minioBucketConfig.getBucketName())
                .objects(objects)
                .build());

        // Actual deletion is performed when iterating over results
        results.forEach(deleteErrorResult -> {
            try {
                deleteErrorResult.get();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<SnowballObject> convertToUploadObjects(List<MultipartFile> files, String owner) throws IOException {
        List<SnowballObject> objects = new ArrayList<>();

        for (MultipartFile file : files) {
            SnowballObject snowballObject = new SnowballObject(
                    getUserRootFolderPrefix(owner) + file.getOriginalFilename(),
                    file.getInputStream(),
                    file.getSize(),
                    null
            );
            objects.add(snowballObject);
        }

        return objects;
    }

    private List<DeleteObject> convertToDeleteObjects(List<MinioObjectDto> files) {
        List<DeleteObject> objects = new ArrayList<>();

        for (MinioObjectDto file : files) {
            objects.add(new DeleteObject(getUserRootFolderPrefix(file.getOwner()) + file.getPath()));
        }

        return objects;
    }
}
