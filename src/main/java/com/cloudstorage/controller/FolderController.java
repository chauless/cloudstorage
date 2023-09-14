package com.cloudstorage.controller;

import com.cloudstorage.dto.FolderDeleteRequest;
import com.cloudstorage.dto.FolderUploadRequest;
import com.cloudstorage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public String uploadFolder(@ModelAttribute("folderUploadRequest") FolderUploadRequest folderUploadRequest) {
        folderService.uploadFolder(folderUploadRequest);
        return "redirect:/";
    }

    @DeleteMapping
    public String deleteFolder(@ModelAttribute("folderDeleteRequest") FolderDeleteRequest folderDeleteRequest) {
        folderService.deleteFolder(folderDeleteRequest);
        return "redirect:/";
    }
}
