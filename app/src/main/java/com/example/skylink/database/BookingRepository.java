package com.example.skylink.database;

import android.os.AsyncTask;

import com.example.skylink.database.entity.Booking;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository extends AsyncTask<Void, Void, List<Booking>> {
    private OnBookingsLoadedListener bookingsLoadedListener;
    private String userId;

    public BookingRepository(OnBookingsLoadedListener bookingsLoadedListener, String userId) {
        this.bookingsLoadedListener = bookingsLoadedListener;
        this.userId = userId;
    }

    @Override
    protected List<Booking> doInBackground(Void... voids) {
        List<Booking> bookingList = new ArrayList<>();
        try{
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("bookings")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               String bookingId = document.getId();
                               Booking booking = document.toObject(Booking.class);
                               booking.setBookingId(bookingId);
                               bookingList.add(booking);
                           }
                           bookingsLoadedListener.onBookingLoaded(bookingList);
                       } else {
                           bookingsLoadedListener.onBookingLoadFailed(task.getException());
                       }
                    });
        } catch (Exception e) {
            bookingsLoadedListener.onBookingLoadFailed(e);
        }

        return bookingList;
    }

    public interface OnBookingsLoadedListener {
        void onBookingLoaded(List<Booking> bookingList);
        void onBookingLoadFailed(Exception e);
    }
}
