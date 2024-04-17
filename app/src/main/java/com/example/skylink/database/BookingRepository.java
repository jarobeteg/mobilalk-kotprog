package com.example.skylink.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.skylink.database.entity.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRepository extends AsyncTask<Void, Void, List<Booking>> {
    private OnBookingsLoadedListener bookingsLoadedListener;
    private OnBookingChangedListener bookingChangedListener;
    private OnBookingDeletedListener bookingDeletedListener;
    private OnBookingAddedListener bookingAddedListener;
    private String userId;

    public BookingRepository(OnBookingsLoadedListener bookingsLoadedListener, String userId) {
        this.bookingsLoadedListener = bookingsLoadedListener;
        this.userId = userId;
    }

    public BookingRepository(OnBookingChangedListener bookingChangedListener) {
        this.bookingChangedListener = bookingChangedListener;
    }

    public BookingRepository(OnBookingDeletedListener bookingDeletedListener) {
        this.bookingDeletedListener = bookingDeletedListener;
    }

    public BookingRepository(OnBookingAddedListener bookingAddedListener) {
        this.bookingAddedListener = bookingAddedListener;
    }

    public void addBooking(Booking booking) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> bookingAddedListener.onBookingAdded(documentReference.getId()))
                .addOnFailureListener(e -> bookingAddedListener.onBookingAddFailed(e));
    }

    public void updateBooking(Booking booking) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("bookings").document(booking.getBookingId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("isFirstClassSeat", booking.isFirstClassSeat());

        documentRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bookingChangedListener.onBookingChanged();
                        } else {
                            bookingChangedListener.onBookingChangeFailed(task.getException());
                        }
                    }
                });
    }

    public void deleteBooking(Booking booking) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("bookings").document(booking.getBookingId());

        documentRef.delete()
                .addOnSuccessListener(v -> bookingDeletedListener.onBookingDeleted(booking))
                .addOnFailureListener(e -> bookingDeletedListener.onBookingDeleteFailed(e));
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

    public interface OnBookingChangedListener {
        void onBookingChanged();
        void onBookingChangeFailed(Exception e);
    }

    public interface OnBookingDeletedListener {
        void onBookingDeleted(Booking booking);
        void onBookingDeleteFailed(Exception e);
    }

    public interface OnBookingAddedListener {
        void onBookingAdded(String bookingId);
        void onBookingAddFailed(Exception e);
    }
}
