package com.example.skylink.database.entity;

public class Aircraft {
    private String model;
    private int firstClassSeats;
    private int secondClassSeats;

    public Aircraft(){}

    public Aircraft(String model, int firstClassSeats, int secondClassSeats) {
        this.model = model;
        this.firstClassSeats = firstClassSeats;
        this.secondClassSeats = secondClassSeats;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFirstClassSeats() {
        return this.firstClassSeats;
    }

    public void setFirstClassSeats(int firstClassSeats) {
        this.firstClassSeats = firstClassSeats;
    }

    public int getSecondClassSeats() {
        return this.secondClassSeats;
    }

    public void setSecondClassSeats(int secondClassSeats) {
        this.secondClassSeats = secondClassSeats;
    }
}
