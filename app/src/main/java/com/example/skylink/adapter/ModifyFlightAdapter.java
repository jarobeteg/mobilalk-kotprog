package com.example.skylink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylink.R;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class ModifyFlightAdapter extends RecyclerView.Adapter<ModifyFlightAdapter.ModifyFlightViewHolder> implements FlightRepository.OnFlightDeletedListener {
    private List<Flight> flightList;
    private Context context;

    public ModifyFlightAdapter(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context;
    }

    @NonNull
    @Override
    public ModifyFlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modify_flight_item, parent, false);
        return new ModifyFlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifyFlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.flightData.setText(setFlightData(flight));
        holder.flightTime.setText(setFlightTime(flight));
        holder.flightDelete.setOnClickListener(v -> deleteFlight(flight));
        holder.flightModify.setOnClickListener(v -> modifyFlight(flight));
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void deleteFlight(Flight flight) {
        FlightRepository flightRepository = new FlightRepository(this);
        flightRepository.deleteFlight(flight);
    }

    private void modifyFlight(Flight flight) {

    }

    private String setFlightData(Flight flight) {
        return flight.getDepartureCity() + " -> " + flight.getDestinationCity();
    }

    private String setFlightTime(Flight flight) {
        return flight.getDepartureTime() + " -> " + flight.getArrivalTime() + " - " + flight.getDate();
    }

    @Override
    public void onFlightDeleted(Flight flight) {
        flightList.remove(flight);
        notifyDataSetChanged();
        showToast(context.getString(R.string.flight_deleted));
    }

    @Override
    public void onFlightDeleteFailed(Exception e) {
        showToast(e.getMessage());
    }

    public static class ModifyFlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightData;
        TextView flightTime;
        ImageButton flightDelete;
        ImageButton flightModify;

        public ModifyFlightViewHolder(@NonNull View view) {
            super(view);
            flightData = view.findViewById(R.id.flight_data);
            flightTime = view.findViewById(R.id.flight_time);
            flightDelete = view.findViewById(R.id.delete_flight);
            flightModify = view.findViewById(R.id.modify_flight);
        }
    }
}
