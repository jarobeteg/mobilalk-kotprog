package com.example.skylink.database.entity;

public class Airline {
    private String name;

    public Airline(){}

    public Airline(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
