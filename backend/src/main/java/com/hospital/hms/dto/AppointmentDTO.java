package com.hospital.hms.dto;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private String id; // Appointment ID
    private Long patientId; // New field for Patient ID
    private Long doctorId; // New field for Doctor ID
    private LocalDateTime appointmentDate; // Appointment Date
    private String status;
    private String patientName;
    private int patientAge;
    private String patientGender;
    private String patientContactNo;

    public AppointmentDTO() {
    }

    public AppointmentDTO(String id, Long patientId, Long doctorId, LocalDateTime appointmentDate, String status, String patientName, int patientAge, String patientGender, String patientContactNo) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.patientContactNo = patientContactNo;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
}