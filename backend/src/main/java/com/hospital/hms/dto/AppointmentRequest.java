package com.hospital.hms.dto;

import com.hospital.hms.model.Patient;
import java.time.LocalDateTime;

public class AppointmentRequest {
    private Patient patient; // Full patient details
    private DoctorDTO doctor; // Minimal doctor details
    private LocalDateTime appointmentDate;
    private String status;
    private String notes;

    public AppointmentRequest() {
    }

    public AppointmentRequest(Patient patient, DoctorDTO doctor, LocalDateTime appointmentDate, String status, String notes) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.notes = notes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}