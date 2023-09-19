package com.cloudstorage.controller;

import com.cloudstorage.dto.FileDeleteRequest;
import com.cloudstorage.dto.FileDownloadRequest;
import com.cloudstorage.dto.FileUploadRequest;
import com.cloudstorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public String uploadFile(@ModelAttribute("fileUploadRequest") FileUploadRequest fileUploadRequest) {
        fileService.uploadFile(fileUploadRequest);
        return "redirect:/";
    }

    @DeleteMapping
    public String deleteFile(@ModelAttribute("fileDeleteRequest") FileDeleteRequest fileDeleteRequest) {
        fileService.deleteFile(fileDeleteRequest);
        return "redirect:/";
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadFile(@ModelAttribute("fileDownloadRequest") FileDownloadRequest fileDownloadRequest) {
        ByteArrayResource file = fileService.downloadFile(fileDownloadRequest);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileDownloadRequest.getName())
                .body(file);
    }
}
