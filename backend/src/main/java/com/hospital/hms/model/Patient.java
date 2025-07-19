package com.hospital.hms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "Patients_Info")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username; // Link to User table

    @Transient // This field will not be persisted in the database
    private String password;

    @Column(name = "Name")
    private String name;

    @Column(name = "Age")
    private int age;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Address")
    private String address;

    @Column(name = "ContactNo")
    private String contactNo;

    @Column(name = "VisitedDoctor")
    private String visitedDoctor;

    @Column(name = "DateOfVisit")
    private LocalDate dateOfVisit;

     @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevent circular reference during serialization
    private List<Billing> billings = new ArrayList<>();


    public Patient() {
    }

    public Patient(String name, String visitedDoctor, LocalDate dateOfVisit, String gender, int age, String address, String contactNo) {
        super();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.contactNo = contactNo;
        this.visitedDoctor = visitedDoctor;
        this.dateOfVisit = dateOfVisit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisitedDoctor() {
        return visitedDoctor;
    }

    public void setVisitedDoctor(String visitedDoctor) {
        this.visitedDoctor = visitedDoctor;
    }

    public LocalDate getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(LocalDate dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
    }
}