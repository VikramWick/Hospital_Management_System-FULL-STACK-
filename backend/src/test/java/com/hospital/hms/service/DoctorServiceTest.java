package com.hospital.hms.service;

import com.hospital.hms.model.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for finding a doctor by username successfully
    @Test
    void findByUsername_Success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setUsername("dr_smith");
        doctor.setName("Dr. Smith");
        doctor.setSpecialist("Cardiology");

        when(doctorRepository.findByUsername("dr_smith")).thenReturn(Optional.of(doctor));

        // Act
        Doctor response = doctorService.findByUsername("dr_smith");

        // Assert
        assertEquals("dr_smith", response.getUsername());
        assertEquals("Dr. Smith", response.getName());
        assertEquals("Cardiology", response.getSpecialist());
        verify(doctorRepository, times(1)).findByUsername("dr_smith");
    }

    // Test for finding a doctor by username when the doctor is not found
    @Test
    void findByUsername_NotFound() {
        // Arrange
        when(doctorRepository.findByUsername("unknown_doctor")).thenReturn(Optional.empty());

        // Act
        Doctor response = doctorService.findByUsername("unknown_doctor");

        // Assert
        assertNull(response);
        verify(doctorRepository, times(1)).findByUsername("unknown_doctor");
    }
}