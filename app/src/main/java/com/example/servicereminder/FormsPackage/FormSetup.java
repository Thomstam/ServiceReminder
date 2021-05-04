package com.example.servicereminder.FormsPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.servicereminder.R;
import com.example.servicereminder.Utilities.CustomAdapter;
import com.example.servicereminder.Utilities.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FormSetup extends AppCompatActivity {

    private final int[] brands = {R.raw.acura, R.raw.chevrolet, R.raw.alfa, R.raw.audi, R.raw.bmw, R.raw.chevrolet, R.raw.dacia, R.raw.daihatsu,
            R.raw.fiat, R.raw.ford, R.raw.honda, R.raw.hyundai, R.raw.kia, R.raw.mazda, R.raw.mercedes};

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String vehicleType;
    private NestedScrollView formScrollView;
    private EditText daysOfUse;
    private EditText averageKmsPerDay;
    private EditText serviceKms;
    private EditText currentKms;
    private EditText platesOfVehicle;
    private TimePicker timeForTheNotification;
    private Spinner notificationTime;
    private Spinner brandIconSelection;
    private boolean isForEdit;
    private Vehicle vehicleForEdit;
    private long notificationTimeForTheService;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_setup);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getBoolean("vehicleBoolean")) {
            vehicles = bundle.getParcelableArrayList("vehicles");
        }
        if (bundle.getBoolean("isForEdit")) {
            isForEdit = true;
            vehicleForEdit = bundle.getParcelable("vehicleForEdit");
            deleteButton();
        } else {
            setBackButton();
        }

        setBrandIconSelection();

        setNotificationSpinner();

        setVehicleRadioButton();

        setFormScrollView();

        setViews();

        setCompleteForm();
    }

    private void finishEditForm(String queryToExecute) {
        Intent intent = new Intent();
        intent.putExtra("vehicle", vehicleForEdit);
        intent.putExtra("QueryToExecute", queryToExecute);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateVehicle() throws ParseException {
        vehicleForEdit.setPlatesOfVehicle(platesOfVehicle.getText().toString());
        vehicleForEdit.setBrandIcon(brands[brandIconSelection.getSelectedItemPosition()]);
        vehicleForEdit.setTypeOfVehicle(vehicleType);
        vehicleForEdit.setCurrentKms(Integer.parseInt(currentKms.getText().toString()));
        vehicleForEdit.setServiceKms(Integer.parseInt(serviceKms.getText().toString()));
        vehicleForEdit.setKmsPerDay(Integer.parseInt(averageKmsPerDay.getText().toString()));
        vehicleForEdit.setUsagePerWeek(Integer.parseInt(daysOfUse.getText().toString()));
        vehicleForEdit.setNotificationTime(notificationTimeForTheService);
        vehicleForEdit.setNotificationSpinnerTimeSelection(notificationTime.getSelectedItem().toString());
        vehicleForEdit.setDateOfTheService(setDayForTheNotification().toString());
        finishEditForm("update");
    }

    private void deleteButton() {
        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> finishEditForm("delete"));
    }

    private void setBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(v -> finish());
    }

    private void setBrandIconSelection() {
        brandIconSelection = findViewById(R.id.brandIcons);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), brands);
        brandIconSelection.setAdapter(customAdapter);
        if (isForEdit) {
            brandIconSelection.setPromptId(vehicleForEdit.getBrandIcon());
        }
    }

    private void setNotificationSpinner() {
        notificationTime = findViewById(R.id.notificationTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.notificationDates, R.layout.spinner_items_custom);
        notificationTime.setAdapter(adapter);
    }

    private void setVehicleRadioButton() {
        RadioGroup vehicleSelection = findViewById(R.id.vehicleSelection);
        if (isForEdit) {
            vehicleType = vehicleForEdit.getTypeOfVehicle();
            if (vehicleType.equals("Car")) {
                vehicleSelection.check(R.id.carVehicle);
            } else if (vehicleType.equals("Truck")) {
                vehicleSelection.check(R.id.truckVehicle);
            } else {
                vehicleSelection.check(R.id.bikeVehicle);
            }
        } else {
            vehicleType = "Car";
        }
        vehicleSelection.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.carVehicle) {
                vehicleType = "Car";
            } else if (checkedId == R.id.truckVehicle) {
                vehicleType = "Truck";
            } else {
                vehicleType = "Bike";
            }
        });
    }

    private void setFormScrollView() {
        formScrollView = findViewById(R.id.formScrollView);
        formScrollView.setSmoothScrollingEnabled(true);
    }

    private void setViews() {
        platesOfVehicle = findViewById(R.id.platesOfVehicle);
        currentKms = findViewById(R.id.currentKms);
        serviceKms = findViewById(R.id.nextServiceKms);
        averageKmsPerDay = findViewById(R.id.kmsPerDay);
        daysOfUse = findViewById(R.id.daysOfUse);
        timeForTheNotification = findViewById(R.id.timeForTheNotification);
        timeForTheNotification.setIs24HourView(true);
        if (isForEdit) {
            platesOfVehicle.setText(vehicleForEdit.getPlatesOfVehicle());
            currentKms.setText(String.valueOf(vehicleForEdit.getCurrentKms()));
            serviceKms.setText(String.valueOf(vehicleForEdit.getServiceKms()));
            averageKmsPerDay.setText(String.valueOf(vehicleForEdit.getKmsPerDay()));
            daysOfUse.setText(String.valueOf(vehicleForEdit.getUsagePerWeek()));
        }
    }

    private long dayCalculatorForServiceInMills() {
        int kmsForTheService = Integer.parseInt(serviceKms.getText().toString()) - Integer.parseInt(currentKms.getText().toString());
        int averageKmsPerWeek = (Integer.parseInt(averageKmsPerDay.getText().toString()) * Integer.parseInt(daysOfUse.getText().toString())) / 7;
        long daysForTheService = kmsForTheService / averageKmsPerWeek + 1;
        return daysForTheService * 86400000;
    }

    private long notificationTimeInMills() throws ParseException {
        Date date = setDayForTheNotification();
        long timeToSubtract = notificationDropDownSelection();
        return date.getTime() - timeToSubtract + timePickerNotificationTime();
    }

    private long notificationDropDownSelection() {
        String[] split = notificationTime.getSelectedItem().toString().split("\\s+");
        return Long.parseLong(split[0]) * 86400000;
    }

    private Date setDayForTheNotification() throws ParseException {
        long test = dayCalculatorForServiceInMills() + System.currentTimeMillis();
        return sdf.parse(sdf.format(test));
    }

    private long timePickerNotificationTime() {
        return timeForTheNotification.getHour() * 3600000 + timeForTheNotification.getMinute() * 60000;
    }


    private void setCompleteForm() {
        ImageButton completeForm = findViewById(R.id.completeForm);
        completeForm.setOnClickListener(v -> {
            if (platesOfVehicle.getText().toString().equals("")) {
                platesOfVehicle.setError("Empty Field");
                formScrollView.scrollTo(platesOfVehicle.getScrollX(), platesOfVehicle.getScrollY());
                return;
            }
            if (currentKms.getText().toString().equals("")) {
                currentKms.setError("Empty Field");
                formScrollView.scrollTo(currentKms.getScrollX(), currentKms.getScrollY());
                return;
            }
            if (serviceKms.getText().toString().equals("")) {
                serviceKms.setError("Empty Field");
                formScrollView.scrollTo(serviceKms.getScrollX(), serviceKms.getScrollY());
                return;
            }
            if (Integer.parseInt(serviceKms.getText().toString()) <= Integer.parseInt(currentKms.getText().toString())) {
                currentKms.setError("Service Kms is bigger or Equal to Current");
                formScrollView.scrollTo(currentKms.getScrollX(), currentKms.getScrollY());
                return;
            }
            if (averageKmsPerDay.getText().toString().equals("")) {
                averageKmsPerDay.setError("Empty Field");
                formScrollView.scrollTo(averageKmsPerDay.getScrollX(), averageKmsPerDay.getScrollY());
                return;
            }
            if (Integer.parseInt(currentKms.getText().toString()) + Integer.parseInt(averageKmsPerDay.getText().toString()) >= Integer.parseInt(serviceKms.getText().toString())) {
                currentKms.setError("Service Kms is too Soon");
                formScrollView.scrollTo(currentKms.getScrollX(), currentKms.getScrollY());
                return;
            }
            if (daysOfUse.getText().toString().equals("")) {
                daysOfUse.setError("Empty Field");
                formScrollView.scrollTo(daysOfUse.getScrollX(), daysOfUse.getScrollY());
                return;
            }
            notificationTimeForTheService = 0;
            try {
                if (notificationTimeInMills() <= System.currentTimeMillis()) {
                    daysOfUse.setError("Notification Time Error");
                    formScrollView.scrollTo(notificationTime.getScrollX(), notificationTime.getScrollY());
                    return;
                }
                notificationTimeForTheService = notificationTimeInMills();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Vehicle vehicle = null;
            try {
                if (isForEdit) {
                    updateVehicle();
                }
                vehicle = new Vehicle(
                        platesOfVehicle.getText().toString(),
                        brands[brandIconSelection.getSelectedItemPosition()],
                        vehicleType,
                        Integer.parseInt(currentKms.getText().toString()),
                        Integer.parseInt(serviceKms.getText().toString()),
                        Integer.parseInt(averageKmsPerDay.getText().toString()),
                        Integer.parseInt(daysOfUse.getText().toString()),
                        notificationTimeForTheService,
                        notificationTime.getSelectedItem().toString(),
                        sdf.format(dayCalculatorForServiceInMills() + System.currentTimeMillis()),
                        false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            completeActivity(vehicle);
        });
    }

    private void completeActivity(Vehicle vehicle) {
        Intent intent = new Intent();
        intent.putExtra("Vehicle", vehicle);
        setResult(RESULT_OK, intent);
        finish();
    }

}