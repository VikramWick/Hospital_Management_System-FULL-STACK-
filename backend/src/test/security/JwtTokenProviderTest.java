package com.hospital.hms.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private String testUsername;
    private String testRole;
    private String generatedToken;

    @BeforeEach
    void setUp() {
        testUsername = "testUser";
        testRole = "ROLE_USER";
        generatedToken = jwtTokenProvider.generateToken(testUsername, testRole);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Act
        String token = jwtTokenProvider.generateToken(testUsername, testRole);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void extractClaims_ShouldReturnValidClaims() {
        // Act
        Claims claims = jwtTokenProvider.extractClaims(generatedToken);

        // Assert
        assertNotNull(claims);
        assertEquals(testUsername, claims.getSubject());
        assertEquals(testRole, claims.get("role"));
    }

    @Test
    void getUsername_ShouldReturnCorrectUsername() {
        // Act
        String extractedUsername = jwtTokenProvider.getUsername(generatedToken);

        // Assert
        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void getRole_ShouldReturnCorrectRole() {
        // Act
        String extractedRole = jwtTokenProvider.getRole(generatedToken);

        // Assert
        assertEquals(testRole, extractedRole);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken(generatedToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void invalidateToken_ShouldMakeTokenInvalid() {
        // Arrange
        String token = jwtTokenProvider.generateToken(testUsername, testRole);
        assertTrue(jwtTokenProvider.isTokenValid(token));

        // Act
        jwtTokenProvider.invalidateToken(token);

        // Assert
        assertFalse(jwtTokenProvider.isTokenValid(token));
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // Act
        boolean isValid = jwtTokenProvider.isTokenValid(generatedToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WithInvalidatedToken_ShouldReturnFalse() {
        // Arrange
        jwtTokenProvider.invalidateToken(generatedToken);

        // Act
        boolean isValid = jwtTokenProvider.isTokenValid(generatedToken);

        // Assert
        assertFalse(isValid);
    }
}
