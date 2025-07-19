package com.hospital.hms.controller;

import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.dto.AppointmentStatusDTO;
import com.hospital.hms.dto.DoctorAppointmentDTO;
import com.hospital.hms.dto.PatientAppointmentDTO;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from the frontend
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(String.valueOf(id));
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return appointmentService.AppointmentsByPatientId(patientId);
    }

    @GetMapping("/doctor/username/{username}")
    public List<DoctorAppointmentDTO> getAppointmentsByDoctorUsername(@PathVariable String username) {
        List<DoctorAppointmentDTO> appointments = appointmentService.getAppointmentsByDoctorUsername(username);
        System.out.println("Fetched appointments for doctor: " + username + " -> " + appointments);
        return appointments;
    }

    @PostMapping
public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
    try {
        Appointment appointment = appointmentService.createAppointment(request);
        return ResponseEntity.ok(appointment);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(null);
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable String id, @RequestBody Appointment updatedAppointment) {
        try {
            Appointment updated = appointmentService.updateAppointment(id, updatedAppointment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
public ResponseEntity<String> updateAppointmentStatus(@PathVariable String id, @RequestParam String status) {
    try {
        appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok("Appointment status updated to: " + status);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/username/{username}")
public List<PatientAppointmentDTO> getAppointmentsByPatientUsername(@PathVariable String username) {
    List<Appointment> appointments = appointmentService.getAppointmentsByPatientUsername(username);
    return appointments.stream()
            .map(appointment -> new PatientAppointmentDTO(
                    appointment.getPatient().getId(), // Patient ID
                    appointment.getId(),
                    appointment.getAppointmentDate(),
                    appointment.getStatus(),
                    appointment.getDoctor().getName(), // Doctor's name
                    appointment.getDoctor().getSpecialist() // Doctor's specialty
            ))
            .collect(Collectors.toList());
}
@PutMapping("/status")
public ResponseEntity<Map<String, String>> updateAppointmentStatus(@RequestBody AppointmentStatusDTO appointmentStatusDTO) {
    try {
        appointmentService.updateAppointmentStatus(appointmentStatusDTO.getAppointmentId(), appointmentStatusDTO.getStatus());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Appointment status updated to: " + appointmentStatusDTO.getStatus());
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
}
