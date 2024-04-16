package com.example.skylink.database.entity;

public class Booking {
    private String bookingId;
    private String  flightId;
    private String  userId;
    private boolean isFirstClassSeat;

    public Booking() {}

    public Booking(String flightId, String userId, boolean isFirstClassSeat) {
        this.flightId = flightId;
        this.userId = userId;
        this.isFirstClassSeat = isFirstClassSeat;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightId() {
        return this.flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFirstClassSeat() {
        return this.isFirstClassSeat;
    }

    public void setFirstClassSeat(boolean isFirstClassSeat) {
        this.isFirstClassSeat= isFirstClassSeat;
    }
}
