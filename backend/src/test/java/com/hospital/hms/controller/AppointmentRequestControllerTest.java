package com.hospital.hms.controller;

import com.hospital.hms.model.AppointmentRequest;
import com.hospital.hms.service.AppointmentRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentRequestControllerTest {

    @Mock
    private AppointmentRequestService appointmentRequestService;

    @InjectMocks
    private AppointmentRequestController appointmentRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("patientId", "1");
        request.put("doctorId", "2");
        request.put("preferredDate", "2025-05-12T10:00:00");
        request.put("notes", "Test appointment");

        AppointmentRequest mockRequest = new AppointmentRequest();
        when(appointmentRequestService.createRequest(any(Long.class), any(Long.class), 
            any(LocalDateTime.class), any(String.class))).thenReturn(mockRequest);

        // Act
        ResponseEntity<?> response = appointmentRequestController.createRequest(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(appointmentRequestService, times(1))
            .createRequest(any(Long.class), any(Long.class), any(LocalDateTime.class), any(String.class));
    }

    @Test
    void createRequest_Failure() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("patientId", "invalid");

        // Act
        ResponseEntity<?> response = appointmentRequestController.createRequest(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllRequests_Success() {
        // Arrange
        List<AppointmentRequest> mockRequests = Arrays.asList(
            new AppointmentRequest(),
            new AppointmentRequest()
        );
        when(appointmentRequestService.getAllRequests()).thenReturn(mockRequests);

        // Act
        ResponseEntity<List<AppointmentRequest>> response = appointmentRequestController.getAllRequests();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(appointmentRequestService, times(1)).getAllRequests();
    }

    @Test
    void getPatientRequests_Success() {
        // Arrange
        Long patientId = 1L;
        List<AppointmentRequest> mockRequests = Arrays.asList(
            new AppointmentRequest(),
            new AppointmentRequest()
        );
        when(appointmentRequestService.getPatientRequests(patientId)).thenReturn(mockRequests);

        // Act
        ResponseEntity<List<AppointmentRequest>> response = 
            appointmentRequestController.getPatientRequests(patientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(appointmentRequestService, times(1)).getPatientRequests(patientId);
    }

    @Test
    void deleteAppointmentRequest_Success() {
        // Arrange
        Long requestId = 1L;
        doNothing().when(appointmentRequestService).deleteRequest(requestId);

        // Act
        ResponseEntity<?> response = appointmentRequestController.deleteAppointmentRequest(requestId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(appointmentRequestService, times(1)).deleteRequest(requestId);
    }

    @Test
    void deleteAppointmentRequest_Failure() {
        // Arrange
        Long requestId = 1L;
        doThrow(new RuntimeException("Delete failed")).when(appointmentRequestService)
            .deleteRequest(requestId);

        // Act
        ResponseEntity<?> response = appointmentRequestController.deleteAppointmentRequest(requestId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to delete appointment request"));
        verify(appointmentRequestService, times(1)).deleteRequest(requestId);
    }
}