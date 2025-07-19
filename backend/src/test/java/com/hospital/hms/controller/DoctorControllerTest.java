package com.hospital.hms.controller;

import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.service.DoctorService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching all doctors
    @Test
    void getAllDoctors_Success() {
        // Arrange
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setName("Dr. Smith");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. John");

        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor1, doctor2));

        // Act
        List<Doctor> response = doctorController.getAllDoctors();

        // Assert
        assertEquals(2, response.size());
        assertEquals("Dr. Smith", response.get(0).getName());
        assertEquals("Dr. John", response.get(1).getName());
        verify(doctorRepository, times(1)).findAll();
    }

    // Test for fetching a doctor by ID
    @Test
    void getDoctorById_Success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dr. Smith", response.getBody().getName());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void getDoctorById_NotFound() {
        // Arrange
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        try {
            doctorController.getDoctorById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Doctor not exist with ID: 1", e.getMessage());
        }
        verify(doctorRepository, times(1)).findById(1L);
    }

    // Test for fetching a doctor by username
    @Test
    void getDoctorByUsername_Success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setUsername("dr_smith");

        when(doctorService.findByUsername("dr_smith")).thenReturn(doctor);

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorByUsername("dr_smith");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dr. Smith", response.getBody().getName());
        verify(doctorService, times(1)).findByUsername("dr_smith");
    }

    @Test
    void getDoctorByUsername_NotFound() {
        // Arrange
        when(doctorService.findByUsername(anyString())).thenReturn(null);

        // Act
        ResponseEntity<Doctor> response = doctorController.getDoctorByUsername("unknown_username");

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody() == null);
        verify(doctorService, times(1)).findByUsername("unknown_username");
    }
}
