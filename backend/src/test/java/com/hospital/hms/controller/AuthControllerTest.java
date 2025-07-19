package com.hospital.hms.controller;

import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.User;
import com.hospital.hms.repository.UserRepository;
import com.hospital.hms.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for user registration
    @Test
    void registerUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setRole(User.Role.Patient);

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<User> response = authController.registerUser(user);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user, response.getBody());
        verify(userRepository, times(1)).findByUsername("john_doe");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setRole(User.Role.Patient);

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = authController.registerUser(user);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, times(1)).findByUsername("john_doe");
        verify(userRepository, never()).save(any(User.class));
    }

    // Test for user login
    @Test
void loginUser_Success() {
    // Arrange
    User user = new User();
    user.setUsername("john_doe");
    user.setPassword("password123");
    user.setRole(User.Role.Patient);

    when(userRepository.findByUsernameAndPassword("john_doe", "password123"))
        .thenReturn(Optional.of(user));
    when(jwtTokenProvider.generateToken("john_doe", "Patient"))
        .thenReturn("mocked-jwt-token");

    // Act
    ResponseEntity<?> response = authController.loginUser(user);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    
    // Get the outer map with "data" key
    Map<String, Object> outerMap = (Map<String, Object>) response.getBody();
    
    // Get the inner map with actual data
    Map<String, Object> dataMap = (Map<String, Object>) outerMap.get("data");
    
    // Verify the inner map contents
    assertEquals("Login successful", dataMap.get("message"));
    assertEquals("mocked-jwt-token", dataMap.get("token"));
    assertEquals(User.Role.Patient, dataMap.get("role"));

    // Verify method calls
    verify(userRepository).findByUsernameAndPassword("john_doe", "password123");
    verify(jwtTokenProvider).generateToken("john_doe", "Patient");
}

    @Test
    void loginUser_InvalidCredentials() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("wrong_password");

        when(userRepository.findByUsernameAndPassword("john_doe", "wrong_password")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.loginUser(user);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Invalid username or password", responseBody.get("error"));
        verify(userRepository, times(1)).findByUsernameAndPassword("john_doe", "wrong_password");
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    void loginUser_InvalidRole() {
        // Arrange
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setRole(User.Role.Patient);

        User loggedInUser = new User();
        loggedInUser.setUsername("john_doe");
        loggedInUser.setPassword("password123");
        loggedInUser.setRole(User.Role.Doctor);

        when(userRepository.findByUsernameAndPassword("john_doe", "password123")).thenReturn(Optional.of(loggedInUser));

        // Act
        ResponseEntity<?> response = authController.loginUser(user);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Invalid role for the user", responseBody.get("error"));
        verify(userRepository, times(1)).findByUsernameAndPassword("john_doe", "password123");
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }

    // Test for fetching user profile by ID
    @Test
    void getUserById_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setRole(User.Role.Patient);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = authController.getUserById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user, response.getBody());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            authController.getUserById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("User not found with ID: 1", e.getMessage());
        }
        verify(userRepository, times(1)).findById(1L);
    }
}