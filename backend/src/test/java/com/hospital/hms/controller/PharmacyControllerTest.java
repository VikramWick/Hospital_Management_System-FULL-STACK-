package com.hospital.hms.controller;

import com.hospital.hms.external.PharmacyServiceClient;
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

class PharmacyControllerTest {

    @InjectMocks
    private PharmacyController pharmacyController;

    @Mock
    private PharmacyServiceClient pharmacyServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching available medicines
    @Test
    void getAvailableMedicines_Success() {
        // Arrange
        Map<String, Object> medicine1 = Map.of(
                "id", 1,
                "name", "Paracetamol",
                "price", 50.0,
                "stock", 100
        );

        Map<String, Object> medicine2 = Map.of(
                "id", 2,
                "name", "Ibuprofen",
                "price", 80.0,
                "stock", 50
        );

        List<Map<String, Object>> mockMedicines = Arrays.asList(medicine1, medicine2);

        when(pharmacyServiceClient.getAvailableMedicines()).thenReturn(mockMedicines);

        // Act
        List<Map<String, Object>> response = pharmacyController.getAvailableMedicines();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Paracetamol", response.get(0).get("name"));
        assertEquals("Ibuprofen", response.get(1).get("name"));
        verify(pharmacyServiceClient, times(1)).getAvailableMedicines();
    }

    @Test
    void getAvailableMedicines_EmptyList() {
        // Arrange
        when(pharmacyServiceClient.getAvailableMedicines()).thenReturn(Arrays.asList());

        // Act
        List<Map<String, Object>> response = pharmacyController.getAvailableMedicines();

        // Assert
        assertEquals(0, response.size());
        verify(pharmacyServiceClient, times(1)).getAvailableMedicines();
    }
}
