package com.example.skylink.database;


import android.os.AsyncTask;

import com.example.skylink.database.entity.Aircraft;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AircraftRepository extends AsyncTask<Void, Void, List<Aircraft>> {
    private FirebaseFirestore db;
    private OnAircraftsLoadedListener aircraftsLoadedListener;
    private OnAircraftAddedListener aircraftAddedListener;

    public AircraftRepository(OnAircraftsLoadedListener aircraftsLoadedListener) {
        this.aircraftsLoadedListener = aircraftsLoadedListener;
    }

    public AircraftRepository(OnAircraftAddedListener aircraftAddedListener) {
        this.aircraftAddedListener = aircraftAddedListener;
        this.db = FirebaseFirestore.getInstance();
    }

    public void addAircraft(Aircraft aircraft) {
        db.collection("aircrafts")
                .add(aircraft)
                .addOnSuccessListener(documentReference -> {
                    if (aircraftAddedListener != null) {
                        aircraftAddedListener.onAircraftAdded(documentReference.getId());
                    }
                })
                .addOnFailureListener(e -> {
                    if (aircraftAddedListener != null) {
                        aircraftAddedListener.onAircraftAddFailed(e);
                    }
                });
    }

    @Override
    protected List<Aircraft> doInBackground(Void... voids) {
        List<Aircraft> aircraftList = new ArrayList<>();
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("aircrafts")
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               Aircraft aircraft = document.toObject(Aircraft.class);
                               aircraftList.add(aircraft);
                           }
                           aircraftsLoadedListener.onAircraftsLoaded(aircraftList);
                       } else {
                           aircraftsLoadedListener.onAircraftsLoadFailed(task.getException());
                       }
                    });
        } catch (Exception e) {
            aircraftsLoadedListener.onAircraftsLoadFailed(e);
        }

        return aircraftList;
    }

    public interface OnAircraftsLoadedListener {
        void onAircraftsLoaded(List<Aircraft> aircrafts);
        void onAircraftsLoadFailed(Exception e);
    }

    public interface OnAircraftAddedListener {
        void onAircraftAdded(String aircraftId);
        void onAircraftAddFailed(Exception e);
    }
}
