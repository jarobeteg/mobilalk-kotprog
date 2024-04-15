package com.example.skylink.ui.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skylink.R;
import com.example.skylink.adapter.ItemAdapter;
import com.example.skylink.adapter.MoreAdapter;
import com.example.skylink.ui.activity.AircraftActivity;
import com.example.skylink.ui.activity.AirlineActivity;
import com.example.skylink.ui.activity.FlightActivity;
import com.example.skylink.ui.activity.ModifyFlightsActivity;
import com.example.skylink.ui.activity.SettingsActivity;
import com.example.skylink.ui.viewmodel.MoreViewModel;

import java.util.Arrays;

public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.moreFragment_recyclerView);
        ItemAdapter[] items = new ItemAdapter[]{
                new ItemAdapter(getString(R.string.title_settings_menu), () -> startActivity(new Intent(requireContext(), SettingsActivity.class))),
                new ItemAdapter(getString(R.string.title_add_new_flight), () -> startActivity(new Intent(requireContext(), FlightActivity.class))),
                new ItemAdapter(getString(R.string.title_add_new_aircraft), () -> startActivity(new Intent(requireContext(), AircraftActivity.class))),
                new ItemAdapter(getString(R.string.title_add_new_airline), () -> startActivity(new Intent(requireContext(), AirlineActivity.class))),
                new ItemAdapter(getString(R.string.modify_flights), () -> startActivity(new Intent(requireContext(), ModifyFlightsActivity.class)))
        };
        MoreAdapter moreAdapter = new MoreAdapter(Arrays.asList(items));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moreAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        // TODO: Use the ViewModel
    }

}