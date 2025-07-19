package com.hospital.hms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"bills", "hibernateLazyInitializer", "handler"}) 
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"bills", "hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnoreProperties({"bills", "hibernateLazyInitializer", "handler"}) // Ignore circular references
    private Appointment appointment;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status; // Unpaid, Paid

    @Column(name = "billing_date")
    private LocalDateTime billingDate;

    // Default constructor
    public Billing() {
        this.billingDate = LocalDateTime.now();
        this.status = "Unpaid";
    }

    // Parameterized constructor
    public Billing(Patient patient, Doctor doctor, Appointment appointment, Double amount, String description, String status) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.billingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDateTime billingDate) {
        this.billingDate = billingDate;
    }

    @Override
public String toString() {
    return "Billing{" +
            "id=" + id +
            ", patient=" + (patient != null ? patient.getName() : "null") +
            ", doctor=" + (doctor != null ? doctor.getName() : "null") +
            ", appointment=" + (appointment != null ? appointment.getId() : "null") +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", status='" + status + '\'' +
            ", billingDate=" + billingDate +
            '}';
}
}