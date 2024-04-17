package com.example.skylink.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.skylink.R;
import com.example.skylink.adapter.BookingsAdapter;
import com.example.skylink.database.BookingRepository;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Booking;
import com.example.skylink.database.entity.Flight;
import com.example.skylink.ui.viewmodel.BookingsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class BookingsFragment extends Fragment implements BookingRepository.OnBookingsLoadedListener, FlightRepository.OnFlightsLoadedListener {
    private List<Booking> bookingList;
    private List<Flight> flightList;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private BookingsAdapter adapter;
    private boolean bookingLoaded = false;
    private boolean flightsLoaded = false;
    private BookingsViewModel mViewModel;

    public static BookingsFragment newInstance() {
        return new BookingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadBookings();
        loadFlights();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BookingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
        loadFlights();
    }

    private void loadBookings() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }
        BookingRepository bookingRepository = new BookingRepository(this, user.getUid());
        bookingRepository.execute();
    }

    private void loadFlights() {
        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.execute();
    }

    private void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void loadBookingData() {
        if (bookingLoaded && flightsLoaded) {
            adapter = new BookingsAdapter(bookingList, flightList, requireContext());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBookingLoaded(List<Booking> bookingList) {
        this.bookingList = bookingList;
        bookingLoaded = true;
        loadBookingData();
    }

    @Override
    public void onBookingLoadFailed(Exception e) {
        showToast(e.getMessage());
    }

    @Override
    public void onFlightsLoaded(List<Flight> flights) {
        this.flightList = flights;
        flightsLoaded = true;
        loadBookingData();
    }

    @Override
    public void onFlightsLoadFailed(Exception e) {
        showToast(e.getMessage());
    }
}