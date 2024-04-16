package com.example.skylink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylink.R;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder>{
    private List<Flight> flightList;
    private Context context;

    public FlightsAdapter(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flights_item, parent, false);
        return new FlightsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightsViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.flightsData.setText(setFlightData(flight));
        holder.flightsSeatData.setText(setFlightSeatData(flight));
        holder.flightsSeatPrices.setText(setFlightSeatPrices(flight));
        holder.flightsTime.setText(setFlightTime(flight));
        holder.flightsBook.setOnClickListener(v -> bookFlight(flight));
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    private String setFlightData(Flight flight) {
        return flight.getDepartureCity() + " -> " + flight.getDestinationCity();
    }

    private String setFlightSeatData(Flight flight) {
        return context.getString(R.string.first_class_seat) + ": " + flight.getVacantFirstClassSeats() + " - " + context.getString(R.string.second_class_seat) + ": " + flight.getVacantSecondClassSeats();
    }

    private String setFlightSeatPrices(Flight flight) {
        return context.getString(R.string.first_class_seat_price) + ": " + Math.round(flight.getPrice() * 1.2) + " - " + context.getString(R.string.second_class_seat_price) + ": " + flight.getPrice();
    }

    private String setFlightTime(Flight flight) {
        return flight.getDepartureTime() + " -> " + flight.getArrivalTime() + " - " + flight.getDate();
    }

    private void bookFlight(Flight flight) {

    }

    public static class FlightsViewHolder extends RecyclerView.ViewHolder {
        TextView flightsData;
        TextView flightsSeatData;
        TextView flightsSeatPrices;
        TextView flightsTime;
        ImageButton flightsBook;

        public FlightsViewHolder(@NonNull View view) {
            super(view);
            flightsData = view.findViewById(R.id.flights_data);
            flightsSeatData = view.findViewById(R.id.flights_seat_data);
            flightsSeatPrices = view.findViewById(R.id.flights_seat_prices);
            flightsTime = view.findViewById(R.id.flights_time);
            flightsBook = view.findViewById(R.id.book_flight);
        }
    }
}
