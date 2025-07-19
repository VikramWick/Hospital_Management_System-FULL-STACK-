package com.hospital.hms.controller;

import com.hospital.hms.model.AppointmentRequest;
import com.hospital.hms.service.AppointmentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointment-requests")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentRequestController {

    @Autowired
    private AppointmentRequestService appointmentRequestService;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> request) {
        try {
            AppointmentRequest created = appointmentRequestService.createRequest(
                Long.parseLong(request.get("patientId").toString()),
                Long.parseLong(request.get("doctorId").toString()),
                java.time.LocalDateTime.parse(request.get("preferredDate").toString()),
                request.get("notes").toString()
            );
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentRequest>> getAllRequests() {
        return ResponseEntity.ok(appointmentRequestService.getAllRequests());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentRequest>> getPatientRequests(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentRequestService.getPatientRequests(patientId));
    }
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteAppointmentRequest(@PathVariable Long requestId) {
    try {
        appointmentRequestService.deleteRequest(requestId);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Failed to delete appointment request: " + e.getMessage());
    }
}
}
