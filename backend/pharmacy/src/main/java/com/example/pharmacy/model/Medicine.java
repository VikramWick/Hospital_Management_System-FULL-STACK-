package com.example.pharmacy.model;

public class Medicine { 
    private String name; 
    private double price; 
 
    public Medicine(String name, double price) { 
        this.name = name; 
        this.price = price; 
    } 
 
    public String getName() { 
        return name; 
    } 
 
    public double getPrice() { 
        return price; 
    } 
}

