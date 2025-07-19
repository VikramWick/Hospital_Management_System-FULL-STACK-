package com.hospital.hms.repository;

import com.hospital.hms.model.Billing;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    @Override
    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    List<Billing> findAll();

    List<Billing> findByPatientId(Long patientId);
    List<Billing> findByStatus(String status);
    List<Billing> findByPatientUsername(String username);
}
