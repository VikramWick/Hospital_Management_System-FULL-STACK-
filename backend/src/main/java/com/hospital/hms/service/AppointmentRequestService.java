package com.hospital.hms.service;

import com.hospital.hms.model.AppointmentRequest;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.model.Patient;
import com.hospital.hms.repository.AppointmentRequestRepository;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentRequestService {

    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public AppointmentRequest createRequest(Long patientId, Long doctorId, LocalDateTime preferredDate, String notes) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AppointmentRequest request = new AppointmentRequest();
        request.setPatient(patient);
        request.setDoctor(doctor);
        request.setPreferredDate(preferredDate);
        request.setNotes(notes);
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());

        return appointmentRequestRepository.save(request);
    }

    public List<AppointmentRequest> getAllRequests() {
        return appointmentRequestRepository.findAll();
    }

    public List<AppointmentRequest> getPatientRequests(Long patientId) {
        return appointmentRequestRepository.findByPatientId(patientId);
    }
    public void deleteRequest(Long requestId) {
    AppointmentRequest request = appointmentRequestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Appointment request not found"));
    appointmentRequestRepository.delete(request);
}
}