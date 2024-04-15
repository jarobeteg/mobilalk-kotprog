package com.example.skylink.database;

import android.os.AsyncTask;

import com.example.skylink.database.entity.Airline;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AirlineRepository extends AsyncTask<Void, Void, List<Airline>> {
    private FirebaseFirestore db;
    private OnAirlinesLoadedListener airlineLoadedListener;
    private OnAirlineAddedListener airlineAddedListener;

    public AirlineRepository(OnAirlinesLoadedListener airlineLoadedListener) {
        this.airlineLoadedListener = airlineLoadedListener;
    }

    public AirlineRepository(OnAirlineAddedListener airlineAddedListener) {
        this.airlineAddedListener = airlineAddedListener;
        this.db = FirebaseFirestore.getInstance();
    }

    public void addAirline(Airline airline) {
        db.collection("airlines")
                .add(airline)
                .addOnSuccessListener(documentReference -> {
                    if (airlineAddedListener != null) {
                        airlineAddedListener.onAirlineAdded(documentReference.getId());
                    }
                })
                .addOnFailureListener(e -> {
                    if (airlineAddedListener != null) {
                        airlineAddedListener.onAirlineAddFailed(e);
                    }
                });
    }

    @Override
    protected List<Airline> doInBackground(Void... voids) {
        List<Airline> airlineList = new ArrayList<>();
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("airlines")
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               Airline airline = document.toObject(Airline.class);
                               airlineList.add(airline);
                           }
                           airlineLoadedListener.onAirlinesLoaded(airlineList);
                       } else {
                           airlineLoadedListener.onAirlinesLoadFailed(task.getException());
                       }
                    });

        } catch (Exception e) {
            airlineLoadedListener.onAirlinesLoadFailed(e);
        }

        return airlineList;
    }

    public interface OnAirlinesLoadedListener {
        void onAirlinesLoaded(List<Airline> airlines);
        void onAirlinesLoadFailed(Exception e);
    }

    public interface OnAirlineAddedListener {
        void onAirlineAdded(String airlineId);
        void onAirlineAddFailed(Exception e);
    }
}
