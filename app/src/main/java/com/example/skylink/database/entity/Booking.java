package com.example.skylink.database.entity;

import com.google.firebase.firestore.DocumentReference;

public class Booking {
    private DocumentReference flightReference;
    private DocumentReference userReference;
    private int seatNumber;
    private boolean isFirstClassSeat;

    public Booking() {}

    public Booking(DocumentReference flightReference, DocumentReference userReference, int seatNumber, boolean isFirstClassSeat) {
        this.flightReference = flightReference;
        this.userReference = userReference;
        this.seatNumber = seatNumber;
        this.isFirstClassSeat = isFirstClassSeat;
    }

    public DocumentReference getFlightReference() {
        return this.flightReference;
    }

    public void setFlightReference(DocumentReference flightReference) {
        this.flightReference = flightReference;
    }

    public DocumentReference getUserReference() {
        return this.userReference;
    }

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
    }

    public int getSeatNumber() {
        return this.seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isFirstClassSeat() {
        return this.isFirstClassSeat;
    }

    public void setFirstClassSeat(boolean isFirstClassSeat) {
        this.isFirstClassSeat= isFirstClassSeat;
    }
}
