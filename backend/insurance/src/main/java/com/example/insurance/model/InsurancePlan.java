package com.example.insurance.model;

public class InsurancePlan { 
    private String name; 
    private double premium; 
 
    public InsurancePlan(String name, double premium) { 
        this.name = name; 
        this.premium = premium;
    } 
 
    public String getName() { 
        return name; 
    } 
 
    public double getPremium() { 
        return premium; 
    } 
} 

