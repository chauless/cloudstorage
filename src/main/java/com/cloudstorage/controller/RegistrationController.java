package com.cloudstorage.controller;

import com.cloudstorage.dto.UserRegistrationRequest;
import com.cloudstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String showRegistrationPage(Model model) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        return "registration";
    }

    @PostMapping
    public String register(@ModelAttribute("userRegistrationRequest") UserRegistrationRequest userRegistrationRequest) {
        userService.register(userRegistrationRequest);
        return "redirect:/login";
    }
}

