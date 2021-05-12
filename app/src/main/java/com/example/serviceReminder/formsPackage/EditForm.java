package com.example.serviceReminder.formsPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.serviceReminder.R;
import com.example.serviceReminder.utilities.CustomAdapter;
import com.example.serviceReminder.utilities.InputFilterMinMax;
import com.example.serviceReminder.utilities.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditForm extends AppCompatActivity {

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
    private Vehicle vehicleForEdit;
    private long notificationTimeForTheService;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_form);

        Bundle bundle = getIntent().getExtras();
        vehicleForEdit = bundle.getParcelable("vehicleForEdit");
        vehicles = bundle.getParcelableArrayList("vehicles");

        setVehicleRadioButton();

        setFormScrollView();

        setViews();

        setNotificationSpinner();

        setBrandIconSelection();

        setCompleteForm();

        setDeleteButton();

        setNotificationTime();

        setBackButton();
    }

    private void setVehicleRadioButton() {
        RadioGroup vehicleSelection = findViewById(R.id.vehicleSelectionEditForm);
        vehicleType = vehicleForEdit.getTypeOfVehicle();
        if (vehicleType.equals("Car")) {
            vehicleSelection.check(R.id.carVehicleEditForm);
        } else if (vehicleType.equals("Truck")) {
            vehicleSelection.check(R.id.truckVehicleEditForm);
        } else {
            vehicleSelection.check(R.id.bikeVehicleEditForm);
        }
        vehicleSelection.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.carVehicleEditForm) {
                vehicleType = "Car";
            } else if (checkedId == R.id.truckVehicleEditForm) {
                vehicleType = "Truck";
            } else {
                vehicleType = "Bike";
            }
        });
    }

    private void setBackButton() {
        ImageButton backButton = findViewById(R.id.backButtonEditForm);
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        backButton.setOnClickListener(v -> {
            backButton.startAnimation(myAnim);
            finish();
        });
    }

    private void setFormScrollView() {
        formScrollView = findViewById(R.id.formScrollViewEditForm);
        formScrollView.setSmoothScrollingEnabled(true);
    }

    private void setViews() {
        platesOfVehicle = findViewById(R.id.platesOfVehicleEditForm);
        platesOfVehicle.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        platesOfVehicle.setText(vehicleForEdit.getPlatesOfVehicle());

        currentKms = findViewById(R.id.currentKmsEditForm);
        currentKms.setText(String.valueOf(vehicleForEdit.getCurrentKms()));

        serviceKms = findViewById(R.id.nextServiceKmsEditForm);
        serviceKms.setText(String.valueOf(vehicleForEdit.getServiceKms()));

        averageKmsPerDay = findViewById(R.id.kmsPerDayEditForm);
        averageKmsPerDay.setText(String.valueOf(vehicleForEdit.getKmsPerDay()));

        daysOfUse = findViewById(R.id.daysOfUseEditForm);
        daysOfUse.setText(String.valueOf(vehicleForEdit.getUsagePerWeek()));
        daysOfUse.setFilters(new InputFilter[]{new InputFilterMinMax(1, 7)});
    }


    private void setNotificationTime() {
        timeForTheNotification = findViewById(R.id.timeForTheNotificationEditForm);
        timeForTheNotification.setIs24HourView(true);
        String timeForTheNotificationVehicle = vehicleForEdit.getHourAndMinOfTheNotification();
        String[] times = timeForTheNotificationVehicle.split(":");
        timeForTheNotification.setHour(Integer.parseInt(times[0]));
        timeForTheNotification.setMinute(Integer.parseInt(times[1]));
    }

    private void setNotificationSpinner() {
        notificationTime = findViewById(R.id.notificationTimeEditForm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.notificationDates, R.layout.spinner_items_custom);
        notificationTime.setAdapter(adapter);
        notificationTime.setSelection(vehicleForEdit.getNotificationSpinnerTimeSelection());
    }

    private void setBrandIconSelection() {
        brandIconSelection = findViewById(R.id.brandIconsEditForm);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), brands);
        brandIconSelection.setAdapter(customAdapter);
        brandIconSelection.setPromptId(vehicleForEdit.getBrandIcon());

    }

    private void setCompleteForm() {
        ImageButton completeForm = findViewById(R.id.completeFormEditForm);
        completeForm.setOnClickListener(v -> {
            if (platesOfVehicle.getText().toString().equals("")) {
                platesOfVehicle.setError("Empty Field");
                formScrollView.scrollTo(platesOfVehicle.getScrollX(), platesOfVehicle.getScrollY());
                return;
            }
            if (alreadyExists()) {
                platesOfVehicle.setError("Vehicle Already Exists");
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
            String plates = platesOfVehicle.getText().toString();
            vehicleForEdit.setPlatesOfVehicle(plates);

            int brandIdIcon = brands[brandIconSelection.getSelectedItemPosition()];
            vehicleForEdit.setBrandIcon(brandIdIcon);

            int currKms = Integer.parseInt(currentKms.getText().toString());
            vehicleForEdit.setCurrentKms(currKms);

            int serKms = Integer.parseInt(serviceKms.getText().toString());
            vehicleForEdit.setServiceKms(serKms);

            int usagePerWeek = Integer.parseInt(daysOfUse.getText().toString());
            vehicleForEdit.setUsagePerWeek(usagePerWeek);

            int kmsPerDay = Integer.parseInt(averageKmsPerDay.getText().toString());
            vehicleForEdit.setKmsPerDay(kmsPerDay);

            vehicleForEdit.setTypeOfVehicle(vehicleType);
            vehicleForEdit.setNotificationTime(notificationTimeForTheService);

            int notificationSpinnerSelection = notificationTime.getSelectedItemPosition();
            vehicleForEdit.setNotificationSpinnerTimeSelection(notificationSpinnerSelection);
            vehicleForEdit.setHourAndMinOfTheNotification(saveHourAndMinutesOfTheNotification());
            finishEditForm("update");
        });
    }

    private boolean alreadyExists() {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlatesOfVehicle().equals(platesOfVehicle.getText().toString()) || vehicle.getId() != vehicleForEdit.getId()) {
                return true;
            }
        }
        return false;
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

    private void finishEditForm(String queryToExecute) {
        Intent intent = new Intent();
        intent.putExtra("vehicle", vehicleForEdit);
        intent.putExtra("QueryToExecute", queryToExecute);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setDeleteButton() {
        ImageButton delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(v -> finishEditForm("delete"));
    }

    private String saveHourAndMinutesOfTheNotification() {
        String hour = String.valueOf(timeForTheNotification.getHour());
        String minute = String.valueOf(timeForTheNotification.getMinute());
        return hour + ":" + minute;
    }
}