package com.hospital.hms.controller;

import com.hospital.hms.external.InsuranceServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InsuranceControllerTest {

    @InjectMocks
    private InsuranceController insuranceController;

    @Mock
    private InsuranceServiceClient insuranceServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching insurance plans
    @Test
    void getInsurancePlans_Success() {
        // Arrange
        Map<String, Object> plan1 = Map.of(
                "id", 1,
                "name", "Basic Health Plan",
                "coverage", "50%"
        );

        Map<String, Object> plan2 = Map.of(
                "id", 2,
                "name", "Premium Health Plan",
                "coverage", "80%"
        );

        List<Map<String, Object>> mockPlans = Arrays.asList(plan1, plan2);

        when(insuranceServiceClient.getInsurancePlans()).thenReturn(mockPlans);

        // Act
        List<Map<String, Object>> response = insuranceController.getInsurancePlans();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Basic Health Plan", response.get(0).get("name"));
        assertEquals("Premium Health Plan", response.get(1).get("name"));
        verify(insuranceServiceClient, times(1)).getInsurancePlans();
    }

    @Test
    void getInsurancePlans_EmptyList() {
        // Arrange
        when(insuranceServiceClient.getInsurancePlans()).thenReturn(Arrays.asList());

        // Act
        List<Map<String, Object>> response = insuranceController.getInsurancePlans();

        // Assert
        assertEquals(0, response.size());
        verify(insuranceServiceClient, times(1)).getInsurancePlans();
    }
}