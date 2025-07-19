package com.hospital.hms.controller;

import com.hospital.hms.dto.PrescriptionDTO;
import com.hospital.hms.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrescriptionControllerTest {

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPrescription_Success() {
        // Arrange
        PrescriptionDTO inputDTO = new PrescriptionDTO();
        inputDTO.setPatientId(1L);
        inputDTO.setDoctorId(2L);
        

        PrescriptionDTO savedDTO = new PrescriptionDTO();
        savedDTO.setId(1L);
        savedDTO.setPatientId(1L);
        savedDTO.setDoctorId(2L);
      

        when(prescriptionService.createPrescription(any(PrescriptionDTO.class)))
            .thenReturn(savedDTO);

        // Act
        ResponseEntity<PrescriptionDTO> response = 
            prescriptionController.createPrescription(inputDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(prescriptionService, times(1))
            .createPrescription(any(PrescriptionDTO.class));
    }

    @Test
    void getPatientPrescriptions_Success() {
        // Arrange
        Long patientId = 1L;
        List<PrescriptionDTO> prescriptions = Arrays.asList(
            new PrescriptionDTO(),
            new PrescriptionDTO()
        );
        when(prescriptionService.getPatientPrescriptions(patientId))
            .thenReturn(prescriptions);

        // Act
        ResponseEntity<List<PrescriptionDTO>> response = 
            prescriptionController.getPatientPrescriptions(patientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(prescriptionService, times(1))
            .getPatientPrescriptions(patientId);
    }

    @Test
    void getDoctorPrescriptions_Success() {
        // Arrange
        Long doctorId = 1L;
        List<PrescriptionDTO> prescriptions = Arrays.asList(
            new PrescriptionDTO(),
            new PrescriptionDTO(),
            new PrescriptionDTO()
        );
        when(prescriptionService.getDoctorPrescriptions(doctorId))
            .thenReturn(prescriptions);

        // Act
        ResponseEntity<List<PrescriptionDTO>> response = 
            prescriptionController.getDoctorPrescriptions(doctorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(prescriptionService, times(1))
            .getDoctorPrescriptions(doctorId);
    }

    @Test
    void getPatientPrescriptions_EmptyList() {
        // Arrange
        Long patientId = 1L;
        when(prescriptionService.getPatientPrescriptions(patientId))
            .thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<PrescriptionDTO>> response = 
            prescriptionController.getPatientPrescriptions(patientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(prescriptionService, times(1))
            .getPatientPrescriptions(patientId);
    }
}