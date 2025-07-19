package com.hospital.hms.dto;

public class DoctorDTO {
    private String name;
    private String specialist;

    public DoctorDTO() {
    }

    public DoctorDTO(String name, String specialist) {
        this.name = name;
        this.specialist = specialist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }
}
