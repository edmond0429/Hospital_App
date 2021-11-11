package com.example.hospital_app;

public class MedicAlarm {
    String medicTitle, medicTime, medicRepetition;
    int medicId;

    public MedicAlarm() {
    }

    public MedicAlarm(String medicTitle, String medicTime, String medicRepetition, int medicId) {
        this.medicTitle = medicTitle;
        this.medicTime = medicTime;
        this.medicRepetition = medicRepetition;
        this.medicId = medicId;
    }

    public String getMedicTitle() {
        return medicTitle;
    }

    public void setMedicTitle(String medicTitle) {
        this.medicTitle = medicTitle;
    }

    public String getMedicTime() {
        return medicTime;
    }

    public void setMedicTime(String medicTime) {
        this.medicTime = medicTime;
    }

    public String getMedicRepetition() {
        return medicRepetition;
    }

    public void setMedicRepetition(String medicRepetition) {
        this.medicRepetition = medicRepetition;
    }

    public int getMedicId() {
        return medicId;
    }

    public void setMedicId(int medicId) {
        this.medicId = medicId;
    }
}
