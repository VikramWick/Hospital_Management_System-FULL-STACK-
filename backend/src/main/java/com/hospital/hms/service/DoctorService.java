package com.hospital.hms.service;

import com.hospital.hms.model.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor findByUsername(String username) {
        return doctorRepository.findByUsername(username).orElse(null);
    }
}