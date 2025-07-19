package com.hospital.hms.service;

import com.hospital.hms.dto.PrescriptionDTO;
import com.hospital.hms.model.Prescription;
import com.hospital.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription();
        
        prescription.setDoctor(doctorRepository.findById(prescriptionDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        
        prescription.setPatient(patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        
        prescription.setAppointment(appointmentRepository.findById(prescriptionDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found")));
        
        prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
        prescription.setMedicines(prescriptionDTO.getMedicines());
        prescription.setInstructions(prescriptionDTO.getInstructions());
        prescription.setNotes(prescriptionDTO.getNotes());
        prescription.setCreatedDate(prescriptionDTO.getCreatedDate());
        
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToDTO(savedPrescription);
    }

    public List<PrescriptionDTO> getDoctorPrescriptions(Long doctorId) {
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(doctorId);
        return prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PrescriptionDTO> getPatientPrescriptions(Long patientId) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);
        return prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setDoctorName(prescription.getDoctor().getName());
        dto.setDiagnosis(prescription.getDiagnosis());
        dto.setMedicines(prescription.getMedicines());
        dto.setInstructions(prescription.getInstructions());
        dto.setNotes(prescription.getNotes());
        dto.setCreatedDate(prescription.getCreatedDate());
        dto.setDoctorId(prescription.getDoctor().getId());
        dto.setPatientId(prescription.getPatient().getId());
        dto.setAppointmentId(prescription.getAppointment().getId());
        return dto;
    }
}