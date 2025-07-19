package com.hospital.hms.service;

import com.hospital.hms.dto.BillingDTO;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.model.Billing;
import com.hospital.hms.repository.BillingRepository;
import com.hospital.hms.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillingRepository billingRepository;

   
    public Optional<Billing> getBillById(Long id) {
        return billingRepository.findById(id);
    }

    public List<Billing> getBillsByPatientId(Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }

    public List<Billing> getBillsByStatus(String status) {
        return billingRepository.findByStatus(status);
    }

    /*public Billing createBill(Billing billing) {
        billing.setBillingDate(LocalDateTime.now());
        return billingRepository.save(billing);
    }*/

     public BillingDTO createBill(BillingDTO billingDTO) {
        // Create new billing
        Billing billing = new Billing();
        
        // Find and set appointment
        Optional<Appointment> appointment = appointmentRepository.findById(billingDTO.getAppointmentId());
        if (!appointment.isPresent()) {
            throw new RuntimeException("Appointment not found with ID: " + billingDTO.getAppointmentId());
        }
        billing.setAppointment(appointment.get());
        
        // Set patient and doctor from appointment
        billing.setPatient(appointment.get().getPatient());
        billing.setDoctor(appointment.get().getDoctor());
        
        // Set other fields
        billing.setAmount(billingDTO.getAmount());
        billing.setDescription(billingDTO.getDescription());
        billing.setStatus(billingDTO.getStatus());
        billing.setBillingDate(LocalDateTime.now());

        //return billingRepository.save(billing);

        // Save the billing entity
        Billing savedBilling = billingRepository.save(billing);
    
    // Convert saved billing to DTO
        BillingDTO savedBillingDTO = new BillingDTO();
        savedBillingDTO.setAppointmentId(savedBilling.getAppointment().getId());
        savedBillingDTO.setAmount(savedBilling.getAmount());
        savedBillingDTO.setDescription(savedBilling.getDescription());
        savedBillingDTO.setStatus(savedBilling.getStatus());
        savedBillingDTO.setBillingDate(savedBilling.getBillingDate());
        savedBillingDTO.setDoctorId(savedBilling.getDoctor().getId());
        savedBillingDTO.setPatientId(savedBilling.getPatient().getId());

        return savedBillingDTO;
    }

    public Billing updateBill(Long id, Billing updatedBilling) {
        return billingRepository.findById(id).map(bill -> {
            bill.setAmount(updatedBilling.getAmount());
            bill.setStatus(updatedBilling.getStatus());
            return billingRepository.save(bill);
        }).orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
    }

    public void deleteBill(Long id) {
        billingRepository.deleteById(id);
    }

    

    public List<Billing> getAllBills() {
        System.out.println("BillingService: Fetching all bills from the repository...");
        List<Billing> bills = billingRepository.findAll();
        System.out.println("BillingService: Fetched bills: " + bills);
    
        // Initialize lazy-loaded relationships
        for (Billing bill : bills) {
            if (bill.getPatient() != null) {
                System.out.println("Initializing Patient: " + bill.getPatient().getName());
            } else {
                System.out.println("Patient is null for Billing ID: " + bill.getId());
            }
    
            if (bill.getDoctor() != null) {
                System.out.println("Initializing Doctor: " + bill.getDoctor().getName());
            } else {
                System.out.println("Doctor is null for Billing ID: " + bill.getId());
            }
    
            if (bill.getAppointment() != null) {
                System.out.println("Initializing Appointment ID: " + bill.getAppointment().getId());
            } else {
                System.out.println("Appointment is null for Billing ID: " + bill.getId());
            }
        }
    
        if (bills.isEmpty()) {
            System.out.println("No bills found in the database.");
        } else {
            System.out.println("Number of bills fetched: " + bills.size());
        }
        return bills;
   
 }

 

}
   
