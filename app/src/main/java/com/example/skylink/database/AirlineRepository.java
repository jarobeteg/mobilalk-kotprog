package com.example.skylink.database;

import android.os.AsyncTask;

import com.example.skylink.database.entity.Airline;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AirlineRepository extends AsyncTask<Void, Void, List<Airline>> {
    private OnAirlinesLoadedListener listener;

    public AirlineRepository(OnAirlinesLoadedListener listener) {
        this.listener = listener;
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
                           listener.onAirlinesLoaded(airlineList);
                       } else {
                           listener.onAirlinesLoadFailed(task.getException());
                       }
                    });

        } catch (Exception e) {
            listener.onAirlinesLoadFailed(e);
        }

        return airlineList;
    }

    public interface OnAirlinesLoadedListener {
        void onAirlinesLoaded(List<Airline> airlines);
        void onAirlinesLoadFailed(Exception e);
    }
}
