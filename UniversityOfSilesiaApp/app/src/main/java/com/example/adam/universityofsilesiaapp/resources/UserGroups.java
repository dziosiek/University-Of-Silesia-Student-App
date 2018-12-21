package com.example.adam.universityofsilesiaapp.resources;

public class UserGroups {
    String specialization;
    String year;
    Integer id;

    public UserGroups() {
    }

    public UserGroups(String specialization, String year, Integer id) {
        this.specialization = specialization;
        this.year = year;
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
