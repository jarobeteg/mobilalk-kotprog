package com.example.skylink.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;

import com.example.skylink.R;
import com.example.skylink.database.AircraftRepository;
import com.example.skylink.database.AirlineRepository;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Aircraft;
import com.example.skylink.database.entity.Airline;
import com.example.skylink.database.entity.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightActivity extends AbsThemeActivity implements AircraftRepository.OnAircraftsLoadedListener, AirlineRepository.OnAirlinesLoadedListener, FlightRepository.OnFlightAddedListener {
   List<Aircraft> allAircrafts;
   List<Airline> allAirlines;
    EditText departureCityEditText;
    EditText destinationCityEditText;
    DatePicker dateDatePicker;
    TimePicker departureTimeTimePicker;
    TimePicker arrivalTimeTimePicker;
    Spinner aircraftSpinner;
    Spinner airlineSpinner;
    EditText priceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        loadAircrafts();
        loadAirlines();

        ImageButton flightCheckmark = findViewById(R.id.new_flight_checkmark);
        Toolbar flightToolbar = findViewById(R.id.new_flight_toolbar);

        departureCityEditText = findViewById(R.id.departure_city);
        destinationCityEditText = findViewById(R.id.destination_city);
        dateDatePicker = findViewById(R.id.date);
        departureTimeTimePicker = findViewById(R.id.departure_time);
        arrivalTimeTimePicker = findViewById(R.id.arrival_time);
        aircraftSpinner = findViewById(R.id.aircraft);
        airlineSpinner = findViewById(R.id.airline);
        priceEditText = findViewById(R.id.price);

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
        String aircraft = aircraftSpinner.getSelectedItem().toString();
        String airline = airlineSpinner.getSelectedItem().toString();
        String arrivalTime = calculateArrivalTime();
        String date = calculateDate();
        String departureCity = departureCityEditText.getText().toString();
        String departureTime = calculateDepartureTime();
        String destinationCity = destinationCityEditText.getText().toString();
        String flightDuration = calculateFlightDuration();
        String strPrice = priceEditText.getText().toString();

        if (aircraft.isEmpty() || airline.isEmpty() || arrivalTime.isEmpty() ||
        date.isEmpty() || departureCity.isEmpty() || departureTime.isEmpty() ||
        destinationCity.isEmpty() || flightDuration.isEmpty() || strPrice.isEmpty()) {
            showToast(getString(R.string.all_fields_must_be_filled));
            return;
        }

        int price = Integer.parseInt(strPrice);
        int vacantFirstClassSeats = 0;
        int vacantSecondClassSeats = 0;

        for (Aircraft res: allAircrafts){
            if (res.getModel().equals(aircraft)) {
                vacantFirstClassSeats = res.getFirstClassSeats();
                vacantSecondClassSeats = res.getSecondClassSeats();
            }
        }

        Flight flight = new Flight(aircraft, airline, arrivalTime,
                date, departureCity, departureTime,
                destinationCity, flightDuration, price,
                vacantFirstClassSeats, vacantSecondClassSeats);

        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.addFlight(flight);

        finish();
    }

    private String calculateArrivalTime() {
        int arrivalTimeHour = arrivalTimeTimePicker.getHour();
        int arrivalTimeMinute = arrivalTimeTimePicker.getMinute();
        String result = arrivalTimeHour + ":" + arrivalTimeMinute;

        if (arrivalTimeMinute == 0) {
            result += "0";
        } else if (arrivalTimeMinute < 10) {
            result = arrivalTimeHour + ":0" + arrivalTimeMinute;
        }

        return result;
    }

    private String calculateDepartureTime() {
        int departureTimeHour = departureTimeTimePicker.getHour();
        int departureTimeMinute = departureTimeTimePicker.getMinute();
        String result = departureTimeHour + ":" + departureTimeMinute;

        if (departureTimeMinute == 0) {
            result += "0";
        } else if (departureTimeMinute < 10) {
            result = departureTimeHour + ":0" + departureTimeMinute;
        }

        return result;
    }

    private String calculateDate() {
        int dateYear = dateDatePicker.getYear();
        int dateMonth = dateDatePicker.getMonth();
        int dateDay = dateDatePicker.getDayOfMonth();
        String strDateMonth = "";
        String strDateDay = "";

        if (dateMonth < 10) {
            strDateMonth = "0";
        }

        if (dateDay < 10) {
            strDateDay = "0";
        }

        return dateYear + "." + strDateMonth + dateMonth + "." + strDateDay + dateDay;
    }

    private String calculateFlightDuration() {
        int arrivalTimeHour = arrivalTimeTimePicker.getHour();
        int arrivalTimeMinute = arrivalTimeTimePicker.getMinute();
        int departureTimeHour = departureTimeTimePicker.getHour();
        int departureTimeMinute = departureTimeTimePicker.getMinute();
        int flightDurationHour = arrivalTimeHour - departureTimeHour;
        int flightDurationMinute = arrivalTimeMinute - departureTimeMinute;

        if (flightDurationHour < 0) {
            flightDurationHour = 24 - Math.abs(flightDurationHour);
        }

        if (flightDurationMinute < 0) {
            flightDurationMinute = 60 - Math.abs(flightDurationMinute);
        }

        String flightDuration = flightDurationHour + getString(R.string.hour);

        if (flightDurationMinute != 0) {
            flightDuration += " " + flightDurationMinute + getString(R.string.minutes);
        }

        return flightDuration;
    }

    private void loadAircrafts() {
        AircraftRepository aircraftRepository = new AircraftRepository(this);
        aircraftRepository.execute();
    }

    @Override
    public void onAircraftsLoaded(List<Aircraft> aircrafts) {
        allAircrafts = aircrafts;
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
        allAirlines = airlines;
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

    @Override
    public void onFlightAdded(String flightId) {
        showToast(getString(R.string.new_flight_added));
    }

    @Override
    public void onFlightAddFailed(Exception e) {
        showToast(e.getMessage());
    }
}