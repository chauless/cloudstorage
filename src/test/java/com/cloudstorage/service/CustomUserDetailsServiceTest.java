package com.cloudstorage.service;

import com.cloudstorage.model.Role;
import com.cloudstorage.model.User;
import com.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void testLoadUserByUsernameWhenUsernameIsValidThenReturnsUserDetails() {
        // Arrange
        String username = "testUser";
        User user = new User(UUID.randomUUID(), username, "password", "test@test.com", Collections.singleton(Role.ROLE_USER));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        var userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(Role.ROLE_USER.name());
    }

    @Test
    public void testLoadUserByUsernameWhenUsernameIsInvalidThenThrowsUsernameNotFoundException() {
        // Arrange
        String username = "invalidUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with username: '" + username + "' not found");
    }
}