package com.nyanmyohtet.springbootstarter.service;

import com.nyanmyohtet.springbootstarter.model.User;
import com.nyanmyohtet.springbootstarter.repository.UserRepository;
import com.nyanmyohtet.springbootstarter.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_success() {
        // Arrange
        String username = "testuser";
        User mockUser = User.builder()
                .id(1L)
                .username(username)
                .passwordHash("hashed_password")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(mockUser.getUsername(), userDetails.getUsername());
        assertEquals(mockUser.getPasswordHash(), userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_userNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
