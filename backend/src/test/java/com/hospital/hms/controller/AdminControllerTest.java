package com.hospital.hms.controller;

import com.hospital.hms.dto.BillingDTO;
import com.hospital.hms.model.*;
import com.hospital.hms.service.BillingService;
import com.hospital.hms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private BillingService billingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for creating a patient
    @Test
    void createPatient_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setUsername("john_doe");
        patient.setPassword("password123");
        patient.setName("John Doe");

        User user = new User(patient.getUsername(), patient.getPassword(), User.Role.Patient);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        ResponseEntity<Map<String, String>> response = adminController.createPatient(patient);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Patient created successfully!", response.getBody().get("message"));
    }

    // Test for creating a doctor
    @Test
void createDoctor_Success() {
    // Arrange
    Doctor doctor = new Doctor();
    doctor.setUsername("dr_smith");
    doctor.setPassword("password123");
    doctor.setName("Dr. Smith");
    doctor.setSpecialist("Cardiology");  // Added required field
    doctor.setAge(35);                   // Added required field
    doctor.setGender("Male");            // Added required field

    User user = new User(doctor.getUsername(), doctor.getPassword(), User.Role.Doctor);

    when(userRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.empty()); 
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

    // Act
    ResponseEntity<Map<String, String>> response = adminController.createDoctor(doctor);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Doctor created successfully!", response.getBody().get("message"));
    
    // Verify calls
    verify(userRepository).findByUsername(doctor.getUsername());
    verify(userRepository).save(any(User.class));
    verify(doctorRepository).save(doctor);
}
    // Test for creating a bill
    @Test
void createBill_Success() {
    // Arrange
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setName("John Doe");

    Doctor doctor = new Doctor();
    doctor.setId(1L);
    doctor.setName("Dr. Smith");

    Appointment appointment = new Appointment();
    appointment.setId("1");  // Make sure this matches the appointmentId in billingDTO

    BillingDTO billingDTO = new BillingDTO();
    billingDTO.setPatientId(1L);  // Add this line
    billingDTO.setDoctorId(1L);
    billingDTO.setAppointmentId("1");  // Make sure this matches the appointment.getId()
    billingDTO.setAmount(500.0);
    billingDTO.setDescription("Consultation Fee");
    billingDTO.setStatus("Paid");
    billingDTO.setBillingDate(LocalDateTime.now());

    // Mock repository calls
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
    when(appointmentRepository.findById("1")).thenReturn(Optional.of(appointment));
    when(billingService.createBill(any(BillingDTO.class))).thenReturn(billingDTO);

    // Act
    ResponseEntity<Map<String, String>> response = adminController.createBill(1L, billingDTO);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Bill created successfully!", response.getBody().get("message"));

    // Verify all repository calls were made
    verify(patientRepository).findById(1L);
    verify(doctorRepository).findById(1L);
    verify(appointmentRepository).findById("1");
    verify(billingService).createBill(any(BillingDTO.class));
}

// Add failure scenario tests
@Test
void createBill_PatientNotFound() {
    BillingDTO billingDTO = new BillingDTO();
    when(patientRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Map<String, String>> response = adminController.createBill(1L, billingDTO);

    assertEquals(400, response.getStatusCode().value());
    assertEquals("Patient not found", response.getBody().get("message"));
}

@Test
void createBill_DoctorNotFound() {
    Patient patient = new Patient();
    patient.setId(1L);
    
    BillingDTO billingDTO = new BillingDTO();
    billingDTO.setDoctorId(1L);
    
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Map<String, String>> response = adminController.createBill(1L, billingDTO);

    assertEquals(400, response.getStatusCode().value());
    assertEquals("Doctor not found", response.getBody().get("message"));
}

@Test
void createBill_AppointmentNotFound() {
    Patient patient = new Patient();
    patient.setId(1L);
    
    Doctor doctor = new Doctor();
    doctor.setId(1L);
    
    BillingDTO billingDTO = new BillingDTO();
    billingDTO.setDoctorId(1L);
    billingDTO.setAppointmentId("1");
    
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
    when(appointmentRepository.findById("1")).thenReturn(Optional.empty());

    ResponseEntity<Map<String, String>> response = adminController.createBill(1L, billingDTO);

    assertEquals(400, response.getStatusCode().value());
    assertEquals("Appointment not found", response.getBody().get("message"));
}

    // Test for deleting a patient
    @Test
void deletePatient_Success() {
    // Arrange
    Patient patient = new Patient();
    patient.setId(1L);
    patient.setUsername("john_doe");
    
    User user = new User();
    user.setUsername("john_doe");

    // Mock findById to return patient
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    // Mock findByUsername to return user
    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
    // Mock delete operations
    doNothing().when(userRepository).delete(any(User.class));
    doNothing().when(patientRepository).deleteById(1L);

    // Act
    ResponseEntity<Map<String, String>> response = adminController.deletePatient(1L);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Patient deleted successfully!", response.getBody().get("message"));
    
    // Verify all repository calls
    verify(patientRepository).findById(1L);
    verify(userRepository).findByUsername("john_doe");
    verify(userRepository).delete(any(User.class));
    verify(patientRepository).deleteById(1L);
}

    @Test
void deleteDoctor_Success() {
    // Arrange
    Doctor doctor = new Doctor();
    doctor.setId(1L);
    doctor.setUsername("dr_smith");
    
    User user = new User();
    user.setUsername("dr_smith");

    // Mock repository calls
    when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
    when(userRepository.findByUsername("dr_smith")).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(any(User.class));
    doNothing().when(doctorRepository).deleteById(1L);

    // Act
    ResponseEntity<Map<String, String>> response = adminController.deleteDoctor(1L);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Doctor deleted successfully!", response.getBody().get("message"));
    
    // Verify all repository calls
    verify(doctorRepository).findById(1L);
    verify(userRepository).findByUsername("dr_smith");
    verify(userRepository).delete(any(User.class));
    verify(doctorRepository).deleteById(1L);
}

    // Test for fetching all bills
    @Test
    void getAllBills_Success() {
        // Arrange
        Billing billing1 = new Billing();
        billing1.setId(1L);
        billing1.setAmount(500.0);
        billing1.setDescription("Consultation Fee");

        Billing billing2 = new Billing();
        billing2.setId(2L);
        billing2.setAmount(1000.0);
        billing2.setDescription("Surgery Fee");

        List<Billing> bills = Arrays.asList(billing1, billing2);

        when(billingService.getAllBills()).thenReturn(bills);

        // Act
        ResponseEntity<List<Billing>> response = adminController.getAllBills();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }
}