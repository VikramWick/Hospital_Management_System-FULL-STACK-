package com.hospital.hms.dto;

public class AppointmentStatusDTO {
    private String appointmentId; // ID of the appointment
    private String status;      // New status of the appointment

    public AppointmentStatusDTO() {
    }

    public AppointmentStatusDTO(String appointmentId, String status) {
        this.appointmentId = appointmentId;
        this.status = status;
    }

    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
