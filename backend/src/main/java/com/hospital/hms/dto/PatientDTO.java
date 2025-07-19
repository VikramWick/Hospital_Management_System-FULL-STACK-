package com.hospital.hms.dto;

public class PatientDTO {
    private String username;
    private Long  id;     // Patient username
    private String name;
    private int age;
    private String gender;
    private String address;
    private String contactNo;

    public PatientDTO() {
    }

    public PatientDTO(Long id,String username, String name, int age, String gender, String address, String contactNo) {
        this.id= id;
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.contactNo = contactNo;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
}