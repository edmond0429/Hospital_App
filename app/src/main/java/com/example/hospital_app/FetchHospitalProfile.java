package com.example.hospital_app;

import java.io.Serializable;

public class FetchHospitalProfile implements Serializable {
    String hospitalName;
    String hospitalAddress;
    String contactNo;
    String hospitalUrl;

    public FetchHospitalProfile(){

    }

    public FetchHospitalProfile(String hospitalName, String hospitalAddress, String contactNo, String hospitalUrl) {
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.contactNo = contactNo;
        this.hospitalUrl = hospitalUrl;
    }

    public String gethospitalName() {
        return hospitalName;
    }

    public void sethospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String gethospitalAddress() {
        return hospitalAddress;
    }

    public void sethospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String contactNo() {
        return contactNo;
    }

    public void contactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String gethospitalUrl() {
        return hospitalUrl;
    }

    public void sethospitalUrl(String hospitalUrl) {
        this.hospitalUrl = hospitalUrl;
    }
}
