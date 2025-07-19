package com.hospital.hms.controller;

import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.dto.AppointmentStatusDTO;
import com.hospital.hms.dto.DoctorAppointmentDTO;
import com.hospital.hms.dto.PatientAppointmentDTO;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.model.Patient;
import com.hospital.hms.service.AppointmentService;
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

class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching all appointments
    @Test
    void getAllAppointments_Success() {
        // Arrange
        Appointment appointment1 = new Appointment();
        appointment1.setId(String.valueOf(1L));
        appointment1.setStatus("Scheduled");

        Appointment appointment2 = new Appointment();
        appointment2.setId(String.valueOf(2L));
        appointment2.setStatus("Completed");

        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(appointment1, appointment2));

        // Act
        List<Appointment> appointments = appointmentController.getAllAppointments();

        // Assert
        assertEquals(2, appointments.size());
        verify(appointmentService, times(1)).getAllAppointments();
    }

    // Test for fetching an appointment by ID
    @Test
    void getAppointmentById_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(String.valueOf(1L));
        appointment.setStatus("Scheduled");

        when(appointmentService.getAppointmentById(String.valueOf(1L))).thenReturn(Optional.of(appointment));

        // Act
        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(appointment, response.getBody());
        verify(appointmentService, times(1)).getAppointmentById(String.valueOf(1L));
    }

    // Test for fetching appointments by patient ID
    @Test
    void getAppointmentsByPatientId_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(String.valueOf(1L));
        appointment.setStatus("Scheduled");

        when(appointmentService.AppointmentsByPatientId(1L)).thenReturn(Arrays.asList(appointment));

        // Act
        List<Appointment> appointments = appointmentController.getAppointmentsByPatientId(1L);

        // Assert
        assertEquals(1, appointments.size());
        verify(appointmentService, times(1)).AppointmentsByPatientId(1L);
    }

    // Test for creating an appointment
    @Test
    void createAppointment_Success() {
        // Arrange
        AppointmentRequest request = new AppointmentRequest();
        //request.setPatientId(1L);
        //request.setDoctorId(1L);
        request.setAppointmentDate(LocalDateTime.now());
        request.setStatus("Scheduled");

        Appointment appointment = new Appointment();
        appointment.setId(String.valueOf(1L));
        appointment.setStatus("Scheduled");

        when(appointmentService.createAppointment(any(AppointmentRequest.class))).thenReturn(appointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.createAppointment(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(appointment, response.getBody());
        verify(appointmentService, times(1)).createAppointment(any(AppointmentRequest.class));
    }

    // Test for updating an appointment
    @Test
    void updateAppointment_Success() {
        // Arrange
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(String.valueOf(1L));
        updatedAppointment.setStatus("Completed");

        when(appointmentService.updateAppointment(anyString(), any(Appointment.class))).thenReturn(updatedAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.updateAppointment(String.valueOf(1L), updatedAppointment);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedAppointment, response.getBody());
        verify(appointmentService, times(1)).updateAppointment(anyString(), any(Appointment.class));
    }

    // Test for updating appointment status
    @Test
    void updateAppointmentStatus_Success() {
        // Arrange
        AppointmentStatusDTO statusDTO = new AppointmentStatusDTO();
        statusDTO.setAppointmentId(String.valueOf(1L));
        statusDTO.setStatus("Completed");

        doNothing().when(appointmentService).updateAppointmentStatus(String.valueOf(1L), "Completed");

        // Act
        ResponseEntity<String> response = appointmentController.updateAppointmentStatus(String.valueOf(1L), "Completed");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Appointment status updated to: Completed", response.getBody());
        verify(appointmentService, times(1)).updateAppointmentStatus(String.valueOf(1L), "Completed");
    }

    // Test for deleting an appointment
    @Test
    void deleteAppointment_Success() {
        // Arrange
        doNothing().when(appointmentService).deleteAppointment(String.valueOf(1L));

        // Act
        ResponseEntity<Void> response = appointmentController.deleteAppointment(String.valueOf(1L));

        // Assert
        assertEquals(204, response.getStatusCode().value());
        verify(appointmentService, times(1)).deleteAppointment(String.valueOf(1L));
    }

    // Test for fetching appointments by patient username
    @Test
    void getAppointmentsByPatientUsername_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setSpecialist("Cardiology");

        Appointment appointment = new Appointment();
        appointment.setId(String.valueOf(1L));
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus("Scheduled");

        when(appointmentService.getAppointmentsByPatientUsername("john_doe")).thenReturn(Arrays.asList(appointment));

        // Act
        List<PatientAppointmentDTO> appointments = appointmentController.getAppointmentsByPatientUsername("john_doe");

        // Assert
        assertEquals(1, appointments.size());
        assertEquals("Dr. Smith", appointments.get(0).getDoctorName());
        verify(appointmentService, times(1)).getAppointmentsByPatientUsername("john_doe");
    }
}
