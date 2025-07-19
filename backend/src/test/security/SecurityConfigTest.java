package com.hospital.hms.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtFilter;

    @Mock
    private AuthenticationConfiguration authConfig;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void filterChain_ShouldConfigureHttpSecurity() throws Exception {
        // Arrange
        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf()).thenReturn(http);
        when(http.disable()).thenReturn(http);
        when(http.authorizeHttpRequests()).thenReturn(http);
        when(http.and()).thenReturn(http);
        when(http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)).thenReturn(http);
        when(http.build()).thenReturn(mock(SecurityFilterChain.class));

        // Act
        SecurityFilterChain result = securityConfig.filterChain(http);

        // Assert
        assertNotNull(result);
    }

    @Test
    void authenticationManager_ShouldReturnAuthenticationManager() throws Exception {
        // Arrange
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(expectedManager);

        // Act
        AuthenticationManager result = securityConfig.authenticationManager(authConfig);

        // Assert
        assertNotNull(result);
        assertEquals(expectedManager, result);
    }

    @Test
    void constructor_ShouldInitializeWithJwtFilter() {
        // Arrange
        JwtAuthenticationFilter mockFilter = mock(JwtAuthenticationFilter.class);

        // Act
        SecurityConfig config = new SecurityConfig(mockFilter);

        // Assert
        assertNotNull(config);
    }
}