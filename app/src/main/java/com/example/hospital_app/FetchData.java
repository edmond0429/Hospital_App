package com.example.hospital_app;

import java.io.Serializable;

public class FetchData implements Serializable {
    String hospitalName;
    String category;
    String doctorName;
    String doctorDescription;
    String doctorUrl;
    String time;

    public FetchData(){

    }

    public FetchData(String hospitalName, String category, String doctorName, String doctorDescription, String doctorUrl, String time) {
        this.hospitalName = hospitalName;
        this.category = category;
        this.doctorName = doctorName;
        this.doctorDescription = doctorDescription;
        this.doctorUrl = doctorUrl;
        this.time = time;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }

    public String getDoctorUrl() {
        return doctorUrl;
    }

    public void setDoctorUrl(String doctorUrl) {
        this.doctorUrl = doctorUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
