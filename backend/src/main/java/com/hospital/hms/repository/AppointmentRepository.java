package com.hospital.hms.repository;

import com.hospital.hms.model.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorUsername(String username);
    List<Appointment> findByPatientUsername(String username);
}
// This code defines a repository interface for managing Appointment entities in a Spring Boot application. It extends JpaRepository, which provides CRUD operations and query methods for the Appointment entity. The interface includes methods to find appointments by patient ID and doctor ID, allowing for easy retrieval of appointment data based on these criteria. The @Repository annotation indicates that this interface is a Spring Data repository, enabling automatic implementation of the defined methods at runtime.
// The AppointmentRepository interface is part of the data access layer of the application, facilitating interaction with the underlying database for appointment-related operations.