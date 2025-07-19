package com.hospital.hms.service;



import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.dto.DoctorAppointmentDTO;
import com.hospital.hms.model.*;
import com.hospital.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> AppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<DoctorAppointmentDTO> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(appointment -> new DoctorAppointmentDTO(
                    appointment.getId(),
                        appointment.getAppointmentDate(),
                        appointment.getPatient().getId(),
                        appointment.getPatient().getName(),
                        appointment.getPatient().getAge(),
                        appointment.getPatient().getGender(),
                        appointment.getPatient().getContactNo(),
                        appointment.getStatus(),
                        appointment.getPatient().getVisitedDoctor(), // Fetch visited doctor from Patient table
                        appointment.getPatient().getDateOfVisit() // Fetch date of visit from Patient table
                ))
                .collect(Collectors.toList());
    }

    public List<DoctorAppointmentDTO> getAppointmentsByDoctorUsername(String username) {
        List<Appointment> appointments = appointmentRepository.findByDoctorUsername(username);
        return appointments.stream()
                .map(appointment -> new DoctorAppointmentDTO(
                    appointment.getId(), // Appointment ID
                    appointment.getAppointmentDate(), // Appointment date
                    appointment.getPatient().getId(), // Patient ID
                    appointment.getPatient().getName(), // Patient name
                    appointment.getPatient().getAge(), // Patient age
                    appointment.getPatient().getGender(), // Patient gender
                    appointment.getPatient().getContactNo(), // Patient contact number
                    appointment.getStatus(), // Appointment status
                    appointment.getPatient().getVisitedDoctor(), // Visited doctor
                    appointment.getPatient().getDateOfVisit() // Date of visit from Patient table
            ))
                
                .collect(Collectors.toList());
    }

    public Appointment createAppointment(AppointmentRequest request) {
        // Find the doctor by name and speciality
        Doctor doctor = doctorRepository.findByNameAndSpecialist(request.getDoctor().getName(), request.getDoctor().getSpecialist())
                .orElseThrow(() -> new RuntimeException("Doctor not found with name: " + request.getDoctor().getName() +
                        " and specialist: " + request.getDoctor().getSpecialist()));

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(request.getPatient()); // Full patient details
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(request.getStatus());
        appointment.setNotes(request.getNotes());

        return appointmentRepository.save(appointment);
    }


    public Appointment updateAppointment(String id, Appointment updatedAppointment) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            appointment.setStatus(updatedAppointment.getStatus());
            appointment.setNotes(updatedAppointment.getNotes());
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }

    public void updateAppointmentStatus(String appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        appointment.setStatus(status); // Update the status
        appointmentRepository.save(appointment); // Save the updated appointment
    }

    public List<Appointment> getAppointmentsByPatientUsername(String username) {
        return appointmentRepository.findByPatientUsername(username);
    }
}
