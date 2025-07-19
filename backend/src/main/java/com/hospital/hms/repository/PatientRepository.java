package com.hospital.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findById(Long id);
    Optional<Patient> findByUsername(String username);

}
