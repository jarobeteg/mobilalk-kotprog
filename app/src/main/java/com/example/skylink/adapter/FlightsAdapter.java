package com.example.skylink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylink.R;
import com.example.skylink.database.BookingRepository;
import com.example.skylink.database.entity.Booking;
import com.example.skylink.database.entity.Flight;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder> implements BookingRepository.OnBookingAddedListener {
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

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }

        View dialogView = LayoutInflater.from(context).inflate(R.layout.book_flight_dialog, null);

        Button bookFirstClassSeatButton = dialogView.findViewById(R.id.book_first_class_seat);
        Button bookSecondClassSeatButton = dialogView.findViewById(R.id.book_second_class_seat);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        bookFirstClassSeatButton.setOnClickListener(v -> {
            Booking booking = new Booking(flight.getFlightId(), user.getUid(), true);
            addBooking(booking);
            dialog.dismiss();
        });

        bookSecondClassSeatButton.setOnClickListener(v -> {
            Booking booking = new Booking(flight.getFlightId(), user.getUid(), false);
            addBooking(booking);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addBooking(Booking booking){
        BookingRepository bookingRepository = new BookingRepository(this);
        bookingRepository.addBooking(booking);
    }

    @Override
    public void onBookingAdded(String bookingId) {
        showToast(context.getString(R.string.flight_booked));
    }

    @Override
    public void onBookingAddFailed(Exception e) {
        showToast(e.getMessage());
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
