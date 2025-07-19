package com.hospital.hms.controller;

import com.hospital.hms.external.InsuranceServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceServiceClient insuranceServiceClient;

    @GetMapping("/plans")
    public List<Map<String, Object>> getInsurancePlans() {
        return insuranceServiceClient.getInsurancePlans();
    }
}
