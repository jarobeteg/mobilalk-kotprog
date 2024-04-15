package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.example.skylink.database.AirlineRepository;
import com.example.skylink.database.entity.Airline;

public class AirlineActivity extends AbsThemeActivity implements AirlineRepository.OnAirlineAddedListener {
    EditText airlineNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline);

        ImageButton airlineCheckmark = findViewById(R.id.new_airline_checkmark);
        Toolbar airlineToolbar = findViewById(R.id.new_airline_toolbar);

        airlineNameEditText = findViewById(R.id.airline_name);

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

        airlineToolbar.setNavigationOnClickListener(v -> onBackPressedCallback.handleOnBackPressed());

        airlineCheckmark.setOnClickListener(v -> {
            addAirline();
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addAirline() {
        String airlineName = airlineNameEditText.getText().toString();

        if (airlineName.isEmpty()) {
            showToast(getString(R.string.all_fields_must_be_filled));
            return;
        }

        Airline airline = new Airline(airlineName);
        AirlineRepository airlineRepository = new AirlineRepository(this);
        airlineRepository.addAirline(airline);

        finish();
    }

    @Override
    public void onAirlineAdded(String airlineId) {
        showToast(getString(R.string.new_airline_added));
    }

    @Override
    public void onAirlineAddFailed(Exception e) {
        showToast(e.getMessage());
    }
}