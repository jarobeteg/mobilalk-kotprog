package com.example.skylink.database;

import android.os.AsyncTask;

import com.example.skylink.database.entity.Flight;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FlightRepository extends AsyncTask<Void, Void, List<Flight>> {
    private FirebaseFirestore db;
    private OnFlightsLoadedListener flightsLoadedListener;
    private OnFlightAddedListener flightAddedListener;
    private OnFlightDeletedListener flightDeletedListener;

    public FlightRepository(OnFlightsLoadedListener flightsLoadedListener) {
        this.flightsLoadedListener = flightsLoadedListener;
    }

    public FlightRepository(OnFlightAddedListener flightAddedListener) {
        this.flightAddedListener = flightAddedListener;
        this.db = FirebaseFirestore.getInstance();
    }

    public FlightRepository(OnFlightDeletedListener flightDeletedListener) {
        this.flightDeletedListener = flightDeletedListener;
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

    public void deleteFlight(Flight flight) {
        DocumentReference flightReference = db.collection("flights").document(flight.getFlightId());

        flightReference.delete()
                .addOnSuccessListener(v -> {
                    flightDeletedListener.onFlightDeleted(flight);
                    updateBookingsForDeletedFlight(flight.getFlightId());
                })
                .addOnFailureListener(e -> flightDeletedListener.onFlightDeleteFailed(e));
    }

    private void updateBookingsForDeletedFlight(String flightId) {
        db.collection("bookings")
                .whereEqualTo("flightId", flightId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        DocumentReference bookingReference = snapshot.getReference();
                        bookingReference.delete();
                    }
                });
    }

    @Override
    protected List<Flight> doInBackground(Void... voids) {
        List<Flight> flightList = new ArrayList<>();
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("flights")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String flightId = document.getId();
                                Flight flight = document.toObject(Flight.class);
                                flight.setFlightId(flightId);
                                flightList.add(flight);
                            }
                            flightsLoadedListener.onFlightsLoaded(flightList);
                        } else {
                            flightsLoadedListener.onFlightsLoadFailed(task.getException());
                        }
                    });
        } catch (Exception e) {
            flightsLoadedListener.onFlightsLoadFailed(e);
        }

        return flightList;
    }

    public interface OnFlightsLoadedListener {
        void onFlightsLoaded(List<Flight> flights);
        void onFlightsLoadFailed(Exception e);
    }

    public interface OnFlightAddedListener {
        void onFlightAdded(String flightId);
        void onFlightAddFailed(Exception e);
    }

    public interface OnFlightDeletedListener {
        void onFlightDeleted(Flight flight);
        void onFlightDeleteFailed(Exception e);
    }
}
