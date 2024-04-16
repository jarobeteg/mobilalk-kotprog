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
import com.example.skylink.adapter.FlightsAdapter;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Flight;
import com.example.skylink.ui.viewmodel.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeFragment extends Fragment implements FlightRepository.OnFlightsLoadedListener {
    private List<Flight> flightList;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private FlightsAdapter adapter;
    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_flights);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadFlights();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void loadFlights() {
        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.execute();
    }

    private void loadFlightsData() {
        adapter = new FlightsAdapter(flightList, requireContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFlightsLoaded(List<Flight> flights) {
        this.flightList = flights;
        loadFlightsData();
    }

    @Override
    public void onFlightsLoadFailed(Exception e) {
        showToast(e.getMessage());
    }
}