package com.hospital.hms.controller;

import com.hospital.hms.dto.PatientDTO;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.Patient;
import com.hospital.hms.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching all patients
    @Test
    void getAllPatients_Success() {
        // Arrange
        Patient patient1 = new Patient();
        patient1.setName("John Doe");
        patient1.setAge(30);
        patient1.setGender("Male");
        patient1.setAddress("123 Main St");
        patient1.setContactNo("1234567890");

        Patient patient2 = new Patient();
        patient2.setName("Jane Doe");
        patient2.setAge(25);
        patient2.setGender("Female");
        patient2.setAddress("456 Elm St");
        patient2.setContactNo("9876543210");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        // Act
        List<Patient> response = patientController.getAllPatients();

        // Assert
        assertEquals(2, response.size());
        assertEquals("John Doe", response.get(0).getName());
        assertEquals("Jane Doe", response.get(1).getName());
        verify(patientRepository, times(1)).findAll();
    }

    // Test for fetching a patient by username
    @Test
    void getPatientByUsername_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setUsername("john_doe");
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setGender("Male");
        patient.setAddress("123 Main St");
        patient.setContactNo("1234567890");

        when(patientRepository.findByUsername("john_doe")).thenReturn(Optional.of(patient));

        // Act
        ResponseEntity<PatientDTO> response = patientController.getPatientByUsername("john_doe");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getName());
        verify(patientRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void getPatientByUsername_NotFound() {
        // Arrange
        when(patientRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        // Act & Assert
        try {
            patientController.getPatientByUsername("unknown_user");
        } catch (ResourceNotFoundException e) {
            assertEquals("Patient not found with username: unknown_user", e.getMessage());
        }
        verify(patientRepository, times(1)).findByUsername("unknown_user");
    }

    // Test for fetching a patient by ID
    @Test
    void getPatientById_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setAge(30);
        patient.setGender("Male");
        patient.setAddress("123 Main St");
        patient.setContactNo("1234567890");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Act
        ResponseEntity<Patient> response = patientController.getDoctorById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_NotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            patientController.getDoctorById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Doctor not exist with ID :1", e.getMessage());
        }
        verify(patientRepository, times(1)).findById(1L);
    }
}
