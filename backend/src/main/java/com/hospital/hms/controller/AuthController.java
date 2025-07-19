package com.hospital.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.hospital.hms.model.User;
import com.hospital.hms.repository.UserRepository;
import com.hospital.hms.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Map;

import com.hospital.hms.exception.*;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtProvider;

    // Register user API
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().build(); // Username already exists
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // Login API


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
    Optional<User> existingUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    if (existingUser.isPresent()) {
        User loggedInUser = existingUser.get();

        // Validate role
        if (!loggedInUser.getRole().equals(user.getRole())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid role for the user"));
        }

        // Generate JWT token with username and role
        String token = jwtProvider.generateToken(loggedInUser.getUsername(), loggedInUser.getRole().toString());

        return ResponseEntity.ok(Collections.singletonMap("data", 
            Map.of(
                "message", "Login successful",
                "token", token,
                "role", loggedInUser.getRole()
            )
        ));
    }
    return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid username or password"));
}


    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return ResponseEntity.ok(Map.of("message", "Password changed successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Old password is incorrect."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found."));
        }
    }

    // Get user profile by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7); // Remove "Bearer " prefix
        jwtProvider.invalidateToken(token); // Invalidate the token
    }
    return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
}
}