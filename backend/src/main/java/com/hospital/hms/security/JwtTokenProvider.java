package com.hospital.hms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenProvider {

    // Generate a secure key for HS512
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Secure key generation
    private final long validityInMilliseconds = 3600000; // 1 hour

    // Generate JWT token with username and role
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username) // Set username as the subject
                .claim("role", role) // Add role as a claim
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds)) // Expiration time
                .signWith(secretKey, SignatureAlgorithm.HS512) // Use the secure key
                .compact();
    }

    // Extract claims from the token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username from the token
    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract role from the token
    public String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private final Set<String> invalidatedTokens = new HashSet<>();

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenValid(String token) {
        return !invalidatedTokens.contains(token) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // Add logic to check if the token is expired
        return false;
    }
}