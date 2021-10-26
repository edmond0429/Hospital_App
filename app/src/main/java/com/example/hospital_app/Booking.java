package com.example.hospital_app;

public class Booking {
    public String patientName;
    public String bookingDate;
    public String bookingTime;
    public String bookingMethod;
    public String doctorName;

    public Booking(){
    }

    public Booking(String patientName, String bookingDate, String bookingTime, String bookingMethod, String doctorName) {
        this.patientName = patientName;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingMethod = bookingMethod;
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingMethod() {
        return bookingMethod;
    }

    public void setBookingMethod(String bookingMethod) {
        this.bookingMethod = bookingMethod;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
