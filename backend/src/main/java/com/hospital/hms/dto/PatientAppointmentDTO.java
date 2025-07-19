package com.hospital.hms.dto;

import java.time.LocalDateTime;

public class PatientAppointmentDTO {
    private Long patientId; // Patient ID
    private String id; // Appointment ID
    private LocalDateTime appointmentDate;
    private String status;
    private String doctorName; // Doctor's name
    private String doctorSpecialist; // Doctor's specialty

    public PatientAppointmentDTO() {
    }

    public PatientAppointmentDTO(Long patientId,String id, LocalDateTime appointmentDate, String status, String doctorName, String doctorSpecialist) {
        this.patientId = patientId;
        this.id = id;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.doctorName = doctorName;
        this.doctorSpecialist = doctorSpecialist;
    }

    // Getters and setters
    public Long getPatientId() {
        return patientId;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSpecialist() {
        return doctorSpecialist;
    }

    public void setDoctorSpecialist(String doctorSpecialist) {
        this.doctorSpecialist = doctorSpecialist;
    }
}
