package com.hospital.hms.controller;

import com.hospital.hms.model.Billing;
import com.hospital.hms.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/billings")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/{id}")
    public Optional<Billing> getBillById(@PathVariable Long id) {
        return billingService.getBillById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<Billing> getBillsByPatientId(@PathVariable Long patientId) {
        return billingService.getBillsByPatientId(patientId);
    }

    @PutMapping("/{id}")
    public Billing updateBill(@PathVariable Long id, @RequestBody Billing billing) {
        return billingService.updateBill(id, billing);
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        billingService.deleteBill(id);
    }

}
