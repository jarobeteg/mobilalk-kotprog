package com.example.skylink.database.entity;

public class Flight {
    private String aircraft;
    private String airline;
    private String arrivalTime;
    private String date;
    private String departureCity;
    private String departureTime;
    private String destinationCity;
    private String flightDuration;
    private int price;

    public Flight() {}

    public Flight(String aircraft, String airline, String arrivalTime,
                  String date, String departureCity, String departureTime,
                  String destinationCity, String flightDuration, int price) {
        this.aircraft = aircraft;
        this.airline = airline;
        this.arrivalTime = arrivalTime;
        this.date = date;
        this.departureCity = departureCity;
        this.departureTime = departureTime;
        this.destinationCity = destinationCity;
        this.flightDuration = flightDuration;
        this.price = price;
    }

    public String getAircraft() {
        return this.aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getAirline() {
        return this.airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartureCity() {
        return this.departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureTime() {
        return this.departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestinationCity() {
        return this.destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getFlightDuration() {
        return this.flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
