package com.example.hospital_app;

public class ReviewModel {
    private String name;
    private String review;
    private double totalStarGiven;
    private String hospitalName;

    public ReviewModel(String name, String review, double totalStarGiven, String hospitalName) {
        this.name = name;
        this.review = review;
        this.totalStarGiven = totalStarGiven;
        this.hospitalName = hospitalName;
    }

    public ReviewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getTotalStarGiven() {
        return totalStarGiven;
    }

    public void setTotalStarGiven(double totalStarGiven) {
        this.totalStarGiven = totalStarGiven;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}