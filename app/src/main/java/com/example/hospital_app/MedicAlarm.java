package com.example.hospital_app;

public class MedicAlarm {
    String medicTitle, medicTime, medicRepetition;

    public MedicAlarm() {
    }

    public MedicAlarm(String medicTitle, String medicTime, String medicRepetition) {
        this.medicTitle = medicTitle;
        this.medicTime = medicTime;
        this.medicRepetition = medicRepetition;
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
}
