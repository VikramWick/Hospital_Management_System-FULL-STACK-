package com.hospital.hms.controller;

import com.hospital.hms.external.PharmacyServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyServiceClient pharmacyServiceClient;

    @GetMapping("/medicines")
    public List<Map<String, Object>> getAvailableMedicines() {
        return pharmacyServiceClient.getAvailableMedicines();
    }
}
