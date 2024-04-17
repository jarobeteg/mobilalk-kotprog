package com.example.skylink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylink.R;
import com.example.skylink.database.FlightRepository;
import com.example.skylink.database.entity.Flight;

import java.util.List;

public class ModifyFlightAdapter extends RecyclerView.Adapter<ModifyFlightAdapter.ModifyFlightViewHolder> implements FlightRepository.OnFlightDeletedListener, FlightRepository.OnFlightChangedListener {
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
        FlightRepository flightRepository = new FlightRepository((FlightRepository.OnFlightDeletedListener) this);
        flightRepository.deleteFlight(flight);
    }

    private void modifyFlight(Flight flight) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.modify_flight_dialog, null);

        EditText departureCityEditText = dialogView.findViewById(R.id.change_departure_city);
        EditText destinationCityEditText = dialogView.findViewById(R.id.change_destination_city);
        DatePicker dateDatePicker = dialogView.findViewById(R.id.change_date);
        TimePicker departureTimeTimePicker = dialogView.findViewById(R.id.change_departure_time);
        TimePicker arrivalTimeTimePicker = dialogView.findViewById(R.id.change_arrival_time);
        Button submitChangesButton = dialogView.findViewById(R.id.submit_changes);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        submitChangesButton.setOnClickListener(v -> {
            if (departureCityEditText.getText().toString().isEmpty() || destinationCityEditText.getText().toString().isEmpty()) {
                showToast(context.getString(R.string.all_fields_must_be_filled));
                return;
            }
            flight.setDepartureCity(departureCityEditText.getText().toString());
            flight.setDestinationCity(destinationCityEditText.getText().toString());
            flight.setDate(calculateDate(dateDatePicker.getYear(), dateDatePicker.getMonth(), dateDatePicker.getDayOfMonth()));
            flight.setDepartureTime(calculateDepartureTime(departureTimeTimePicker.getHour(), departureTimeTimePicker.getMinute()));
            flight.setArrivalTime(calculateArrivalTime(arrivalTimeTimePicker.getHour(), arrivalTimeTimePicker.getMinute()));
            flight.setFlightDuration(calculateFlightDuration(arrivalTimeTimePicker.getHour(), arrivalTimeTimePicker.getMinute(), departureTimeTimePicker.getHour(), departureTimeTimePicker.getMinute()));
            updateFlight(flight);
            dialog.dismiss();
        });

        dialog.show();
    }

    private String calculateFlightDuration(int arrivalTimeHour, int arrivalTimeMinute,
                                           int departureTimeHour, int departureTimeMinute) {
        int flightDurationHour = arrivalTimeHour - departureTimeHour;
        int flightDurationMinute = arrivalTimeMinute - departureTimeMinute;

        if (flightDurationHour < 0) {
            flightDurationHour = 24 - Math.abs(flightDurationHour);
        }

        if (flightDurationMinute < 0) {
            flightDurationMinute = 60 - Math.abs(flightDurationMinute);
        }

        String flightDuration = flightDurationHour + context.getString(R.string.hour);

        if (flightDurationMinute != 0) {
            flightDuration += " " + flightDurationMinute + context.getString(R.string.minutes);
        }

        return flightDuration;
    }

    private String calculateArrivalTime(int arrivalTimeHour, int arrivalTimeMinute) {
        String result = arrivalTimeHour + ":" + arrivalTimeMinute;

        if (arrivalTimeMinute == 0) {
            result += "0";
        } else if (arrivalTimeMinute < 10) {
            result = arrivalTimeHour + ":0" + arrivalTimeMinute;
        }

        return result;
    }

    private String calculateDepartureTime(int departureTimeHour, int departureTimeMinute) {
        String result = departureTimeHour + ":" + departureTimeMinute;

        if (departureTimeMinute == 0) {
            result += "0";
        } else if (departureTimeMinute < 10) {
            result = departureTimeHour + ":0" + departureTimeMinute;
        }

        return result;
    }

    private String calculateDate(int year, int month, int day) {
        String strDateMonth = "";
        String strDateDay = "";

        if (month < 10) {
            strDateMonth = "0";
        }

        if (day < 10) {
            strDateDay = "0";
        }

        return year + "." + strDateMonth + month + "." + strDateDay + day;
    }

    private String setFlightData(Flight flight) {
        return flight.getDepartureCity() + " -> " + flight.getDestinationCity();
    }

    private String setFlightTime(Flight flight) {
        return flight.getDepartureTime() + " -> " + flight.getArrivalTime() + " - " + flight.getDate();
    }

    private void updateFlight(Flight flight) {
        FlightRepository flightRepository = new FlightRepository((FlightRepository.OnFlightChangedListener) this);
        flightRepository.updateFlight(flight);
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

    @Override
    public void onFlightChanged() {
        notifyDataSetChanged();
        showToast(context.getString(R.string.flight_modified));
    }

    @Override
    public void onFlightChangeFailed(Exception e) {
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
