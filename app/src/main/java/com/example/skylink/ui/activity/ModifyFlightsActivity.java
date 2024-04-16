package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylink.R;
import com.example.skylink.adapter.ModifyFlightAdapter;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class ModifyFlightsActivity extends AbsThemeActivity implements FlightRepository.OnFlightsLoadedListener {
    List<Flight> allFlights;
    private RecyclerView recyclerView;
    private ModifyFlightAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flights);
        loadFlights();

        Toolbar modifyFlightToolbar = findViewById(R.id.modify_flights_toolbar);

        recyclerView = findViewById(R.id.recycler_view_modify_flights);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    private void loadFlights() {
        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.execute();
    }

    private void loadFlightData() {
        adapter = new ModifyFlightAdapter(allFlights, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFlightsLoaded(List<Flight> flights) {
        allFlights = flights;
        loadFlightData();
    }

    @Override
    public void onFlightsLoadFailed(Exception e) {
        showToast(e.getMessage());
    }
}