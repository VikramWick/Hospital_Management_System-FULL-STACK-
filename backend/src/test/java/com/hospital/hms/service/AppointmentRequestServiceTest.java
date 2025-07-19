package com.hospital.hms.service;

import com.hospital.hms.model.AppointmentRequest;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.model.Patient;
import com.hospital.hms.repository.AppointmentRequestRepository;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AppointmentRequestServiceTest {

    @InjectMocks
    private AppointmentRequestService appointmentRequestService;

    @Mock
    private AppointmentRequestRepository appointmentRequestRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Success() {
        // Arrange
        Long patientId = 1L;
        Long doctorId = 1L;
        LocalDateTime preferredDate = LocalDateTime.now().plusDays(1);
        String notes = "Test appointment";

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setName("Dr. Smith");

        AppointmentRequest expectedRequest = new AppointmentRequest();
        expectedRequest.setPatient(patient);
        expectedRequest.setDoctor(doctor);
        expectedRequest.setPreferredDate(preferredDate);
        expectedRequest.setNotes(notes);
        expectedRequest.setStatus("PENDING");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(appointmentRequestRepository.save(any(AppointmentRequest.class))).thenReturn(expectedRequest);

        // Act
        AppointmentRequest result = appointmentRequestService.createRequest(patientId, doctorId, preferredDate, notes);

        // Assert
        assertNotNull(result);
        assertEquals(patient, result.getPatient());
        assertEquals(doctor, result.getDoctor());
        assertEquals(preferredDate, result.getPreferredDate());
        assertEquals(notes, result.getNotes());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void createRequest_PatientNotFound() {
        // Arrange
        Long patientId = 1L;
        Long doctorId = 1L;
        LocalDateTime preferredDate = LocalDateTime.now().plusDays(1);
        String notes = "Test appointment";

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentRequestService.createRequest(patientId, doctorId, preferredDate, notes);
        });
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void createRequest_DoctorNotFound() {
        // Arrange
        Long patientId = 1L;
        Long doctorId = 1L;
        LocalDateTime preferredDate = LocalDateTime.now().plusDays(1);
        String notes = "Test appointment";

        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentRequestService.createRequest(patientId, doctorId, preferredDate, notes);
        });
        assertEquals("Doctor not found", exception.getMessage());
    }

    @Test
    void getAllRequests_Success() {
        // Arrange
        AppointmentRequest request1 = new AppointmentRequest();
        request1.setId(1L);
        AppointmentRequest request2 = new AppointmentRequest();
        request2.setId(2L);

        List<AppointmentRequest> expectedRequests = Arrays.asList(request1, request2);
        when(appointmentRequestRepository.findAll()).thenReturn(expectedRequests);

        // Act
        List<AppointmentRequest> result = appointmentRequestService.getAllRequests();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRequests, result);
    }

    @Test
    void getPatientRequests_Success() {
        // Arrange
        Long patientId = 1L;
        AppointmentRequest request1 = new AppointmentRequest();
        request1.setId(1L);
        AppointmentRequest request2 = new AppointmentRequest();
        request2.setId(2L);

        List<AppointmentRequest> expectedRequests = Arrays.asList(request1, request2);
        when(appointmentRequestRepository.findByPatientId(patientId)).thenReturn(expectedRequests);

        // Act
        List<AppointmentRequest> result = appointmentRequestService.getPatientRequests(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRequests, result);
    }
}