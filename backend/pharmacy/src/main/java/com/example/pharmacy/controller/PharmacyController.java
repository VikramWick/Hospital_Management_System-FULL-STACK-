package com.example.pharmacy.controller;


import java.util.Arrays; 
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pharmacy.model.Medicine; 
 
 
@CrossOrigin(origins = "http://localhost:4200") 
@RestController 
@RequestMapping("/api/pharmacy") 
public class PharmacyController { 
 
    @GetMapping("/medicines") 
    public List<Medicine> getAvailableMedicines() { 
        return Arrays.asList( 
            new Medicine("Paracetamol", 10.0), 
            new Medicine("Ibuprofen", 15.0), 
            new Medicine("Amoxicillin", 20.0), 
            new Medicine("Dolo", 12.0), 
            new Medicine("Omeprazole", 25.0), 
            new Medicine("Cetirizine", 12.0), 
            new Medicine("Metformin", 18.0), 
            new Medicine("Amlodipine", 30.0), 
            new Medicine("Azithromycin", 45.0), 
            new Medicine("Montelukast", 35.0), 
            new Medicine("Pantoprazole", 28.0), 
            new Medicine("Doxycycline", 22.0), 
            new Medicine("Aspirin", 8.0), 
            new Medicine("Metoprolol", 32.0), 
            new Medicine("Losartan", 40.0), 
            new Medicine("Sertraline", 50.0) 
        ); 
    } 
} 

