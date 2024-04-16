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

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder> implements BookingRepository.OnBookingChangedListener, BookingRepository.OnBookingDeletedListener {
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

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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

    private void deleteBooking(Booking booking) {
        BookingRepository bookingRepository = new BookingRepository((BookingRepository.OnBookingDeletedListener) this);
        bookingRepository.deleteBooking(booking);
    }

    private void modifyBooking(Booking booking) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.modify_booking_dialog, null);

        Button changeToFirstClassSeatButton = dialogView.findViewById(R.id.change_to_first_class_seat);
        Button changeToSecondClassSeatButton = dialogView.findViewById(R.id.change_to_second_class_seat);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (booking.isFirstClassSeat()) {
            changeToFirstClassSeatButton.setVisibility(View.GONE);
            changeToSecondClassSeatButton.setOnClickListener(v -> {
                booking.setFirstClassSeat(false);
                updateBooking(booking);
                dialog.dismiss();
            });
        } else {
            changeToSecondClassSeatButton.setVisibility(View.GONE);
            changeToFirstClassSeatButton.setOnClickListener(v -> {
                booking.setFirstClassSeat(true);
                updateBooking(booking);
                dialog.dismiss();
            });
        }

        dialog.show();
    }

    private void updateBooking(Booking booking) {
        BookingRepository bookingRepository = new BookingRepository((BookingRepository.OnBookingChangedListener) this);
        bookingRepository.updateBooking(booking);
    }

    @Override
    public void onBookingChanged() {
        notifyDataSetChanged();
        showToast(context.getString(R.string.booking_changed));
    }

    @Override
    public void onBookingChangeFailed(Exception e) {
        showToast(e.getMessage());
    }

    @Override
    public void onBookingDeleted(Booking booking) {
        bookingList.remove(booking);
        notifyDataSetChanged();
        showToast(context.getString(R.string.booking_deleted));
    }

    @Override
    public void onBookingDeleteFailed(Exception e) {
        showToast(e.getMessage());
    }

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
