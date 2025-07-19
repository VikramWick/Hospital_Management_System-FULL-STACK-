package com.hospital.hms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DoctorAppointmentDTO {
    private String id; // Appointment ID

   
    private LocalDateTime appointmentDate;
    private Long patientId; // New field for Patient ID
    private String patientName;
    private int patientAge;
    private String patientGender;
    private String patientContactNo;
    private String status;
    private String visitedDoctor; // Field for visited doctor
    private LocalDate dateOfVisit; // Field for date of visit

    public DoctorAppointmentDTO() {
    }

    public DoctorAppointmentDTO(String id,LocalDateTime appointmentDate,Long patientID, String patientName, int patientAge, String patientGender, String patientContactNo, String status, String visitedDoctor, LocalDate dateOfVisit) {
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.patientId = patientID;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.patientContactNo = patientContactNo;
        this.status = status;
        this.visitedDoctor = visitedDoctor;
        this.dateOfVisit = dateOfVisit;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientContactNo() {
        return patientContactNo;
    }

    public void setPatientContactNo(String patientContactNo) {
        this.patientContactNo = patientContactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisitedDoctor() {
        return visitedDoctor;
    }

    public void setVisitedDoctor(String visitedDoctor) {
        this.visitedDoctor = visitedDoctor;
    }

    public LocalDate getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(LocalDate dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }
}