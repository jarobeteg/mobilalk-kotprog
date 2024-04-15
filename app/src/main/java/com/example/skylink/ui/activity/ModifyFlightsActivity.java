package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;

public class ModifyFlightsActivity extends AbsThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flights);

        ImageButton modifyFlightCheckmark = findViewById(R.id.modify_flights_checkmark);
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

        modifyFlightCheckmark.setOnClickListener(v -> {
            modifyFlight();
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void modifyFlight() {
        finish();
    }
}