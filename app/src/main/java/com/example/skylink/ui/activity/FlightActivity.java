package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.example.skylink.database.AircraftRepository;
import com.example.skylink.database.AirlineRepository;
import com.example.skylink.database.entity.Aircraft;
import com.example.skylink.database.entity.Airline;

import java.util.ArrayList;
import java.util.List;

public class FlightActivity extends AbsThemeActivity implements AircraftRepository.OnAircraftsLoadedListener, AirlineRepository.OnAirlinesLoadedListener {
    Spinner aircraftSpinner;
    Spinner airlineSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        loadAircrafts();
        loadAirlines();

        ImageButton flightCheckmark = findViewById(R.id.new_flight_checkmark);
        Toolbar flightToolbar = findViewById(R.id.new_flight_toolbar);

        aircraftSpinner = findViewById(R.id.aircraft);
        airlineSpinner = findViewById(R.id.airline);

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

        flightToolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        flightCheckmark.setOnClickListener(v -> {
            addFlight();
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addFlight() {
        finish();
    }

    private void loadAircrafts() {
        AircraftRepository aircraftRepository = new AircraftRepository(this);
        aircraftRepository.execute();
    }

    @Override
    public void onAircraftsLoaded(List<Aircraft> aircrafts) {
        List<String> modelList = new ArrayList<>();
        for (Aircraft aircraft: aircrafts) {
            modelList.add(aircraft.getModel());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aircraftSpinner.setAdapter(adapter);
    }

    @Override
    public void onAircraftsLoadFailed(Exception e) {
        showToast(e.getMessage());
    }

    private void loadAirlines() {
        AirlineRepository airlineRepository = new AirlineRepository(this);
        airlineRepository.execute();
    }

    @Override
    public void onAirlinesLoaded(List<Airline> airlines) {
        List<String> nameList = new ArrayList<>();
        for (Airline airline: airlines) {
            nameList.add(airline.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        airlineSpinner.setAdapter(adapter);
    }

    @Override
    public void onAirlinesLoadFailed(Exception e) {
        showToast(e.getMessage());
    }
}