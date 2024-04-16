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
import com.example.skylink.database.entity.Booking;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder> {
    private List<Booking> bookingList;
    private List<Flight> flightList;
    private Context context;

    public BookingsAdapter(List<Booking> bookingList, List<Flight> flightList, Context context) {
        this.bookingList = bookingList;
        this.flightList = flightList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_item, parent, false);
        return new BookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.bookingData.setText(setBookingData(booking));
        holder.bookingSeatType.setText(setBookingSeatType(booking));
        holder.bookingTime.setText(setBookingTime(booking));
        holder.bookingDelete.setOnClickListener(v -> deleteBooking(booking));
        holder.bookingModify.setOnClickListener(v -> modifyBooking(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private String setBookingData(Booking booking) {
        String departureCity = "";
        String destinationCity = "";
        for (Flight flight: flightList) {
            if (flight.getFlightId().equals(booking.getFlightId())) {
                departureCity = flight.getDepartureCity();
                destinationCity = flight.getDestinationCity();
            }
        }

        return departureCity + " -> " + destinationCity;
    }

    private String setBookingSeatType(Booking booking) {
        if (booking.isFirstClassSeat()) {
            return context.getString(R.string.first_class_seat);
        } else {
            return context.getString(R.string.second_class_seat);
        }
    }

    private String setBookingTime(Booking booking) {
        String departureTime = "";
        String arrivalTime = "";
        String date = "";
        for (Flight flight: flightList) {
            if (flight.getFlightId().equals(booking.getFlightId())) {
                departureTime = flight.getDepartureTime();
                arrivalTime = flight.getDepartureTime();
                date = flight.getDepartureTime();
            }
        }

        return departureTime + " -> " + arrivalTime + " - " + date;
    }

    private void deleteBooking(Booking booking) {}

    private void modifyBooking(Booking booking) {}


    public static class BookingsViewHolder extends RecyclerView.ViewHolder {
        TextView bookingData;
        TextView bookingSeatType;
        TextView bookingTime;
        ImageButton bookingDelete;
        ImageButton bookingModify;

        public BookingsViewHolder(@NonNull View view) {
            super(view);
            bookingData = view.findViewById(R.id.booking_data);
            bookingSeatType = view.findViewById(R.id.booking_seat_type);
            bookingTime = view.findViewById(R.id.booking_time);
            bookingDelete = view.findViewById(R.id.delete_booking);
            bookingModify = view.findViewById(R.id.modify_booking);
        }
    }
}
