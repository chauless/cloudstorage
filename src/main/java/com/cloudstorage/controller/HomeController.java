package com.cloudstorage.controller;

import com.cloudstorage.dto.FileUploadRequest;
import com.cloudstorage.dto.MinioObjectDto;
import com.cloudstorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.cloudstorage.utils.BreadcrumbsUtils.getBreadcrumbLinksForPath;
import static com.cloudstorage.utils.BreadcrumbsUtils.getFolderNamesForPath;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;

    @GetMapping
    public String getHomePage(@AuthenticationPrincipal User user,
                              @RequestParam(value = "path", required = false, defaultValue = "") String path,
                              Model model) {

        if (user != null) {
            List<MinioObjectDto> userFiles = fileService.getUserFiles(user.getUsername(), path);
            model.addAttribute("files", userFiles);
            model.addAttribute("username", user.getUsername());
        }

        model.addAttribute("fileUploadRequest", new FileUploadRequest());
        model.addAttribute("breadcrumbLinks", getBreadcrumbLinksForPath(path));
        model.addAttribute("breadcrumbFolders", getFolderNamesForPath(path));

        return "home";
    }
}
