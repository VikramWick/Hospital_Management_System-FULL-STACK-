package com.hospital.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hospital.hms.repository.*;
import java.util.List;
import com.hospital.hms.model.Patient;
import com.hospital.hms.dto.PatientDTO;
import com.hospital.hms.exception.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // Get all patients' information
    // This method retrieves all patients' information from the database and maps it
    // to a DTO.
    @GetMapping("/patients-info")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/patients-info/username/{username}")
    public ResponseEntity<PatientDTO> getPatientByUsername(@PathVariable String username) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username: " + username));
        PatientDTO patientDTO = new PatientDTO(
                patient.getId(),
                patient.getUsername(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getAddress(),
                patient.getContactNo());
        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping("/patients-info/{id}")
    public ResponseEntity<Patient> getDoctorById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not exist with ID :" + id));
        return ResponseEntity.ok(patient);
    }

}
