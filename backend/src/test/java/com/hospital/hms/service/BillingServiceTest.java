package com.hospital.hms.service;

import com.hospital.hms.model.Appointment;
import com.hospital.hms.model.Billing;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.model.Patient;
import com.hospital.hms.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import com.hospital.hms.dto.BillingDTO; // Add this import if BillingDTO exists in the specified package
import com.hospital.hms.repository.AppointmentRepository; // Add this import for AppointmentRepository
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    @InjectMocks
    private BillingService billingService;

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

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
        billing.setStatus("Paid");

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        // Act
        Optional<Billing> response = billingService.getBillById(1L);

        // Assert
        assertEquals(billing, response.get());
        verify(billingRepository, times(1)).findById(1L);
    }

    @Test
    void getBillById_NotFound() {
        // Arrange
        when(billingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billingService.getBillById(1L).orElseThrow(() -> new RuntimeException("Billing not found with id: 1"));
        });
        assertEquals("Billing not found with id: 1", exception.getMessage());
        verify(billingRepository, times(1)).findById(1L);
    }

    // Test for fetching bills by patient ID
    @Test
    void getBillsByPatientId_Success() {
        // Arrange
        Billing billing1 = new Billing();
        billing1.setId(1L);
        billing1.setAmount(500.0);

        Billing billing2 = new Billing();
        billing2.setId(2L);
        billing2.setAmount(1000.0);

        when(billingRepository.findByPatientId(1L)).thenReturn(Arrays.asList(billing1, billing2));

        // Act
        List<Billing> response = billingService.getBillsByPatientId(1L);

        // Assert
        assertEquals(2, response.size());
        verify(billingRepository, times(1)).findByPatientId(1L);
    }

    // Test for fetching bills by status
    @Test
    void getBillsByStatus_Success() {
        // Arrange
        Billing billing1 = new Billing();
        billing1.setId(1L);
        billing1.setStatus("Paid");

        Billing billing2 = new Billing();
        billing2.setId(2L);
        billing2.setStatus("Paid");

        when(billingRepository.findByStatus("Paid")).thenReturn(Arrays.asList(billing1, billing2));

        // Act
        List<Billing> response = billingService.getBillsByStatus("Paid");

        // Assert
        assertEquals(2, response.size());
        verify(billingRepository, times(1)).findByStatus("Paid");
    }

    // Test for creating a bill
    @Test
void createBill_Success() {
    // Arrange
    // Create test appointment with patient and doctor
    Patient patient = new Patient();
    patient.setId(1L);
    
    Doctor doctor = new Doctor();
    doctor.setId(1L);
    
    Appointment appointment = new Appointment();
    appointment.setId("1");
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);

    // Create input DTO
    BillingDTO inputDTO = new BillingDTO();
    inputDTO.setAppointmentId("1");
    inputDTO.setAmount(500.0);
    inputDTO.setStatus("Unpaid");
    inputDTO.setDescription("Test Bill");

    // Create billing entity that will be saved
    Billing billingToSave = new Billing();
    billingToSave.setAmount(500.0);
    billingToSave.setStatus("Unpaid");
    billingToSave.setDescription("Test Bill");
    billingToSave.setAppointment(appointment);
    billingToSave.setPatient(patient);
    billingToSave.setDoctor(doctor);
    billingToSave.setBillingDate(LocalDateTime.now());

    // Mock repository responses
    when(appointmentRepository.findById("1")).thenReturn(Optional.of(appointment));
    when(billingRepository.save(any(Billing.class))).thenReturn(billingToSave);

    // Act
    BillingDTO response = billingService.createBill(inputDTO);

    // Assert
    assertNotNull(response);
    assertEquals(inputDTO.getAmount(), response.getAmount());
    assertEquals(inputDTO.getStatus(), response.getStatus());
    assertEquals(inputDTO.getDescription(), response.getDescription());
    assertEquals(inputDTO.getAppointmentId(), response.getAppointmentId());
    assertEquals(doctor.getId(), response.getDoctorId());
    assertEquals(patient.getId(), response.getPatientId());

    // Verify repository calls
    verify(appointmentRepository).findById("1");
    verify(billingRepository).save(any(Billing.class));
}

    // Test for updating a bill
    @Test
    void updateBill_Success() {
        // Arrange
        Billing existingBilling = new Billing();
        existingBilling.setId(1L);
        existingBilling.setAmount(500.0);
        existingBilling.setStatus("Unpaid");

        Billing updatedBilling = new Billing();
        updatedBilling.setAmount(600.0);
        updatedBilling.setStatus("Paid");

        when(billingRepository.findById(1L)).thenReturn(Optional.of(existingBilling));
        when(billingRepository.save(any(Billing.class))).thenReturn(updatedBilling);

        // Act
        Billing response = billingService.updateBill(1L, updatedBilling);

        // Assert
        assertEquals(600.0, response.getAmount());
        assertEquals("Paid", response.getStatus());
        verify(billingRepository, times(1)).findById(1L);
        verify(billingRepository, times(1)).save(any(Billing.class));
    }

    @Test
    void updateBill_NotFound() {
        // Arrange
        when(billingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            billingService.updateBill(1L, new Billing());
        });
        assertEquals("Billing not found with id: 1", exception.getMessage());
        verify(billingRepository, times(1)).findById(1L);
        verify(billingRepository, never()).save(any(Billing.class));
    }

    // Test for deleting a bill
    @Test
    void deleteBill_Success() {
        // Arrange
        doNothing().when(billingRepository).deleteById(1L);

        // Act
        billingService.deleteBill(1L);

        // Assert
        verify(billingRepository, times(1)).deleteById(1L);
    }

    // Test for fetching all bills
    @Test
    void getAllBills_Success() {
        // Arrange
        Billing billing1 = new Billing();
        billing1.setId(1L);
        billing1.setAmount(500.0);

        Billing billing2 = new Billing();
        billing2.setId(2L);
        billing2.setAmount(1000.0);

        when(billingRepository.findAll()).thenReturn(Arrays.asList(billing1, billing2));

        // Act
        List<Billing> response = billingService.getAllBills();

        // Assert
        assertEquals(2, response.size());
        verify(billingRepository, times(1)).findAll();
    }
}