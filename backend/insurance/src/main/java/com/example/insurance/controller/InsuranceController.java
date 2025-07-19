package com.example.insurance.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.model.InsurancePlan;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController 
@RequestMapping("/api/insurance") 
public class InsuranceController { 
 
    @GetMapping("/plans") 
    public List<InsurancePlan> getInsurancePlans() { 
        return Arrays.asList( 
            new InsurancePlan("Basic Health Insurance", 500.0), 
            new InsurancePlan("Premium Health Insurance", 1200.0), 
            new InsurancePlan("Family Health Insurance", 2000.0), 
            new InsurancePlan("Senior Citizen Health Insurance", 800.0), 
            new InsurancePlan("Term Life Insurance", 1000.0), 
            new InsurancePlan("Whole Life Insurance", 1500.0), 
            new InsurancePlan("Critical Illness Insurance", 750.0), 
            new InsurancePlan("Dental Insurance", 300.0), 
            new InsurancePlan("Vision Insurance", 250.0) 
        ); 
    } 
 
     
} 

