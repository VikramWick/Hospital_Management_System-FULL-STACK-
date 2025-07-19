package com.hospital.hms.controller;

import com.hospital.hms.model.Billing;
import com.hospital.hms.service.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BillingControllerTest {

    @InjectMocks
    private BillingController billingController;

    @Mock
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching a bill by ID
    @Test
    void getBillById_Success() {
        // Arrange
        Billing billing = new Billing();
        billing.setId(1L);
        billing.setAmount(500.0);
        billing.setDescription("Consultation Fee");

        when(billingService.getBillById(1L)).thenReturn(Optional.of(billing));

        // Act
        Optional<Billing> response = billingController.getBillById(1L);

        // Assert
        assertEquals(billing, response.get());
        verify(billingService, times(1)).getBillById(1L);
    }

    // Test for fetching bills by patient ID
    @Test
    void getBillsByPatientId_Success() {
        // Arrange
        Billing billing1 = new Billing();
        billing1.setId(1L);
        billing1.setAmount(500.0);
        billing1.setDescription("Consultation Fee");

        Billing billing2 = new Billing();
        billing2.setId(2L);
        billing2.setAmount(1000.0);
        billing2.setDescription("Surgery Fee");

        when(billingService.getBillsByPatientId(1L)).thenReturn(Arrays.asList(billing1, billing2));

        // Act
        List<Billing> response = billingController.getBillsByPatientId(1L);

        // Assert
        assertEquals(2, response.size());
        assertEquals(billing1, response.get(0));
        assertEquals(billing2, response.get(1));
        verify(billingService, times(1)).getBillsByPatientId(1L);
    }

    // Test for updating a bill
    @Test
    void updateBill_Success() {
        // Arrange
        Billing existingBilling = new Billing();
        existingBilling.setId(1L);
        existingBilling.setAmount(500.0);
        existingBilling.setDescription("Consultation Fee");

        Billing updatedBilling = new Billing();
        updatedBilling.setId(1L);
        updatedBilling.setAmount(600.0);
        updatedBilling.setDescription("Updated Consultation Fee");

        when(billingService.updateBill(anyLong(), any(Billing.class))).thenReturn(updatedBilling);

        // Act
        Billing response = billingController.updateBill(1L, updatedBilling);

        // Assert
        assertEquals(updatedBilling, response);
        verify(billingService, times(1)).updateBill(anyLong(), any(Billing.class));
    }

    // Test for deleting a bill
    @Test
    void deleteBill_Success() {
        // Arrange
        doNothing().when(billingService).deleteBill(1L);

        // Act
        billingController.deleteBill(1L);

        // Assert
        verify(billingService, times(1)).deleteBill(1L);
    }
}