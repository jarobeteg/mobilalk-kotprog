package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class ModifyFlightsActivity extends AbsThemeActivity implements FlightRepository.OnFlightsLoadedListener {
    List<Flight> allFlights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flights);
        loadFlights();

        Toolbar modifyFlightToolbar = findViewById(R.id.modify_flights_toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };

        modifyFlightToolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void modifyFlight() {
        finish();
    }

    private void loadFlights() {
        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.execute();
    }

    @Override
    public void onFlightsLoaded(List<Flight> flights) {
        allFlights = flights;
    }

    @Override
    public void onFlightsLoadFailed(Exception e) {
        showToast(e.getMessage());
    }
}