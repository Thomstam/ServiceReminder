package com.example.serviceReminder.utilities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serviceReminder.R;
import com.example.serviceReminder.database.Vehicle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Vehicle vehicle;

    public static BottomSheetFragment newInstance(Vehicle vehicleToShow) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("vehicle", vehicleToShow);
        bottomSheetFragment.setArguments(bundle);
        return bottomSheetFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        assert getArguments() != null;
        vehicle = getArguments().getParcelable("vehicle");


        return inflater.inflate(R.layout.bottom_sheet_vehicle_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView platesOfVehicle = view.findViewById(R.id.bottomSheetPlatesNumber);
        platesOfVehicle.setText(vehicle.getPlatesOfVehicle());

        TextView averageKmsPerDay = view.findViewById(R.id.bottomSheetAverageKmsPerDayNumber);
        averageKmsPerDay.setText(String.valueOf(vehicle.getKmsPerDay()));

        TextView usagePerWeek = view.findViewById(R.id.bottomSheetWeeklyUsageNumber);
        usagePerWeek.setText(String.valueOf(vehicle.getUsagePerWeek()));

        TextView currentKms = view.findViewById(R.id.bottomSheetCurrentKmsNumber);
        currentKms.setText(String.valueOf(vehicle.getCurrentKms()));

        TextView serviceKms = view.findViewById(R.id.bottomSheetServiceKmsNumber);
        serviceKms.setText(String.valueOf(vehicle.getServiceKms()));

        TextView notificationDay = view.findViewById(R.id.bottomSheetNotificationDayNumber);
        notificationDay.setText(vehicle.getHourAndMinOfTheNotification());
    }
}
