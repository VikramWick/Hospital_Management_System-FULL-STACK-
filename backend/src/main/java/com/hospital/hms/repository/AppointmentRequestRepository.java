package com.hospital.hms.repository;

import com.hospital.hms.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
    List<AppointmentRequest> findByPatientId(Long patientId);
    List<AppointmentRequest> findByDoctorId(Long doctorId);
    List<AppointmentRequest> findByStatus(String status);
}
