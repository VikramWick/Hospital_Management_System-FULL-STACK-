package com.hospital.hms.dto;

import java.time.LocalDateTime;

public class BillingDTO {
    private Long doctorId;
    private Long patientId; // Add patientId field
    private String appointmentId;
    private Double amount;
    private String description;
    private String status;
    private LocalDateTime billingDate;

    // Default constructor
    public BillingDTO() {
        this.billingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() { // Add getter for patientId
        return patientId;
    }

    public void setPatientId(Long patientId) { // Add setter for patientId
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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
}