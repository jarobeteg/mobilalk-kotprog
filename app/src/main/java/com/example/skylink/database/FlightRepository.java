package com.example.skylink.database;

import com.example.skylink.database.entity.Flight;
import com.google.firebase.firestore.FirebaseFirestore;

public class FlightRepository {
    private FirebaseFirestore db;
    private OnFlightAddedListener flightAddedListener;

    public FlightRepository(OnFlightAddedListener flightAddedListener) {
        this.flightAddedListener = flightAddedListener;
        this.db = FirebaseFirestore.getInstance();
    }

    public void addFlight(Flight flight) {
        db.collection("flights")
                .add(flight)
                .addOnSuccessListener(documentReference -> {
                    if (flightAddedListener != null) {
                        flightAddedListener.onFlightAdded(documentReference.getId());
                    }
                })
                .addOnFailureListener(e -> {
                    if (flightAddedListener != null) {
                        flightAddedListener.onFlightAddFailed(e);
                    }
                });

    }

    public interface OnFlightAddedListener {
        void onFlightAdded(String flightId);
        void onFlightAddFailed(Exception e);
    }
}
