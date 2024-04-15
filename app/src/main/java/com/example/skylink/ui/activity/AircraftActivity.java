package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.example.skylink.database.AircraftRepository;
import com.example.skylink.database.entity.Aircraft;

public class AircraftActivity extends AbsThemeActivity implements AircraftRepository.OnAircraftAddedListener {
    EditText aircraftModelEditText;
    EditText firstClassSeatsEditText;
    EditText secondClassSeatsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircraft);

        ImageButton aircraftCheckmark = findViewById(R.id.new_aircraft_checkmark);
        Toolbar aircraftToolbar = findViewById(R.id.new_aircraft_toolbar);

        aircraftModelEditText = findViewById(R.id.aircraft_model);
        firstClassSeatsEditText = findViewById(R.id.first_class_seats);
        secondClassSeatsEditText = findViewById(R.id.second_class_seats);

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

        aircraftToolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        aircraftCheckmark.setOnClickListener(v -> {
            addAircraft();
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addAircraft() {
        String aircraftModel = aircraftModelEditText.getText().toString();
        String strFirstClassSeats = firstClassSeatsEditText.getText().toString();
        String strSecondClassSeats = secondClassSeatsEditText.getText().toString();

        if (aircraftModel.isEmpty() || strFirstClassSeats.isEmpty() || strSecondClassSeats.isEmpty()) {
            showToast(getString(R.string.all_fields_must_be_filled));
            return;
        }

        int firstClassSeats = Integer.parseInt(strFirstClassSeats);
        int secondClassSeats = Integer.parseInt(strSecondClassSeats);

        Aircraft aircraft = new Aircraft(aircraftModel, firstClassSeats, secondClassSeats);
        AircraftRepository aircraftRepository = new AircraftRepository(this);
        aircraftRepository.addAircraft(aircraft);

        finish();
    }

    @Override
    public void onAircraftAdded(String aircraftId) {
        showToast(getString(R.string.new_aircraft_added));
    }

    @Override
    public void onAircraftAddFailed(Exception e) {
        showToast(e.getMessage());
    }
}