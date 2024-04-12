package com.example.skylink.database;


import android.os.AsyncTask;

import com.example.skylink.database.entity.Aircraft;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AircraftRepository extends AsyncTask<Void, Void, List<Aircraft>> {
    private OnAircraftsLoadedListener listener;

    public AircraftRepository(OnAircraftsLoadedListener listener) {
        this.listener = listener;
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
                           listener.onAircraftsLoaded(aircraftList);
                       } else {
                           listener.onAircraftsLoadFailed(task.getException());
                       }
                    });
        } catch (Exception e) {
            listener.onAircraftsLoadFailed(e);
        }

        return aircraftList;
    }


    public interface OnAircraftsLoadedListener {
        void onAircraftsLoaded(List<Aircraft> aircrafts);
        void onAircraftsLoadFailed(Exception e);
    }
}
