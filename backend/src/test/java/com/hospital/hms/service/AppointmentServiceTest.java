package com.hospital.hms.service;

import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.dto.DoctorAppointmentDTO;
import com.hospital.hms.dto.DoctorDTO;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.model.Patient;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

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

        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment1, appointment2));

        // Act
        List<Appointment> response = appointmentService.getAllAppointments();

        // Assert
        assertEquals(2, response.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    // Test for fetching an appointment by ID
    
    @Test
    void getAppointmentById_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId("APID0001");
        appointment.setStatus("Scheduled");

        when(appointmentRepository.findById("APID0001")).thenReturn(Optional.of(appointment));

        // Act
        Optional<Appointment> response = appointmentService.getAppointmentById("APID0001");

        // Assert
        assertEquals(appointment, response.get());
        verify(appointmentRepository, times(1)).findById("APID0001");
    }

    @Test
    void getAppointmentById_NotFound() {
        // Arrange
        when(appointmentRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.getAppointmentById("APID0001").orElseThrow(() -> 
                new RuntimeException("Appointment not found with id: APID0001"));
        });
        assertEquals("Appointment not found with id: APID0001", exception.getMessage());
        verify(appointmentRepository, times(1)).findById("APID0001");
    }

    @Test
    void createAppointment_Success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setSpecialist("Cardiology");

        Patient patient = new Patient();
        patient.setName("John Doe");

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr. Smith");
        doctorDTO.setSpecialist("Cardiology");

        AppointmentRequest request = new AppointmentRequest();
        request.setDoctor(doctorDTO);
        request.setPatient(patient);
        request.setAppointmentDate(LocalDateTime.now());
        request.setStatus("Scheduled");
        request.setNotes("Initial consultation");

        Appointment appointment = new Appointment();
        appointment.setId("APID0001");
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(request.getStatus());
        appointment.setNotes(request.getNotes());

        when(doctorRepository.findByNameAndSpecialist(anyString(), anyString()))
            .thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment response = appointmentService.createAppointment(request);

        // Assert
        assertEquals(appointment, response);
        verify(doctorRepository, times(1)).findByNameAndSpecialist(anyString(), anyString());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_Success() {
        // Arrange
        Appointment existingAppointment = new Appointment();
        existingAppointment.setId("APID0001");
        existingAppointment.setStatus("Scheduled");

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setStatus("Completed");

        when(appointmentRepository.findById("APID0001")).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        // Act
        Appointment response = appointmentService.updateAppointment("APID0001", updatedAppointment);

        // Assert
        assertEquals("Completed", response.getStatus());
        verify(appointmentRepository, times(1)).findById("APID0001");
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void deleteAppointment_Success() {
        // Arrange
        doNothing().when(appointmentRepository).deleteById("APID0001");

        // Act
        appointmentService.deleteAppointment("APID0001");

        // Assert
        verify(appointmentRepository, times(1)).deleteById("APID0001");
    }

    @Test
    void updateAppointmentStatus_Success() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId("APID0001");
        appointment.setStatus("Scheduled");

        when(appointmentRepository.findById("APID0001")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        appointmentService.updateAppointmentStatus("APID0001", "Completed");

        // Assert
        assertEquals("Completed", appointment.getStatus());
        verify(appointmentRepository, times(1)).findById("APID0001");
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentStatus_NotFound() {
        // Arrange
        when(appointmentRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.updateAppointmentStatus("APID0001", "Completed");
        });
        assertEquals("Appointment not found with ID: APID0001", exception.getMessage());
        verify(appointmentRepository, times(1)).findById("APID0001");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}