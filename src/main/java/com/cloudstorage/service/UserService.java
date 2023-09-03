package com.cloudstorage.service;

import com.cloudstorage.dto.UserRegistrationRequest;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.cloudstorage.model.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserRegistrationRequest userRegistrationRequest) {
        User user = new User(
                userRegistrationRequest.getUsername(),
                passwordEncoder.encode(userRegistrationRequest.getPassword()),
                userRegistrationRequest.getEmail(),
                Set.of(ROLE_USER)
        );

        userRepository.save(user);
    }
}
