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
import android.widget.Toast;

import com.example.skylink.R;
import com.example.skylink.adapter.ItemAdapter;
import com.example.skylink.adapter.MoreAdapter;
import com.example.skylink.ui.activity.AircraftActivity;
import com.example.skylink.ui.activity.AirlineActivity;
import com.example.skylink.ui.activity.FlightActivity;
import com.example.skylink.ui.activity.ModifyFlightsActivity;
import com.example.skylink.ui.activity.SettingsActivity;
import com.example.skylink.ui.viewmodel.MoreViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<ItemAdapter> items = new ArrayList<>();
        items.add(new ItemAdapter(getString(R.string.title_settings_menu), () -> startActivity(new Intent(requireContext(), SettingsActivity.class))));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !user.isAnonymous()) {
            items.add(new ItemAdapter(getString(R.string.title_add_new_flight), () -> startActivity(new Intent(requireContext(), FlightActivity.class))));
            items.add(new ItemAdapter(getString(R.string.title_add_new_aircraft), () -> startActivity(new Intent(requireContext(), AircraftActivity.class))));
            items.add(new ItemAdapter(getString(R.string.title_add_new_airline), () -> startActivity(new Intent(requireContext(), AirlineActivity.class))));
            items.add(new ItemAdapter(getString(R.string.modify_flights), () -> startActivity(new Intent(requireContext(), ModifyFlightsActivity.class))));
        } else {
            showToast(getString(R.string.create_profile_to_access_more_functions));
        }

        MoreAdapter moreAdapter = new MoreAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moreAdapter);
    }

    private void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        // TODO: Use the ViewModel
    }

}