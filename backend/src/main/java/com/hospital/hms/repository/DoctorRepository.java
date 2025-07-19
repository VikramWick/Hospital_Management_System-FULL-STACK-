package com.hospital.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.hospital.hms.model.Doctor;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByNameAndSpecialist(String name, String speciality);
    Optional<Doctor> findByUsername(String username);
}
