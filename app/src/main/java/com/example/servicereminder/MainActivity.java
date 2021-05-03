package com.example.servicereminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.servicereminder.FormsPackage.FormSetup;
import com.example.servicereminder.MainFragments.FavoritesScreenFragment;
import com.example.servicereminder.MainFragments.HomeScreenFragments;
import com.example.servicereminder.MainFragments.UpcomingServicesScreenFragment;
import com.example.servicereminder.NotificationSetup.ServiceNotification;
import com.example.servicereminder.Utilities.SettingsFragment;
import com.example.servicereminder.Utilities.Vehicle;
import com.example.servicereminder.database.VehicleViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private VehicleViewModel vehicleViewModel;
    private final static int REQUEST_FORM_SETUP = 101;
    private final static String NAME_FOR_NOTIFICATION_CHANNEL = "DefaultNotificationChannel";
    private final static String ID_FOR_NOTIFICATION_CHANNEL = "ServiceReminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationChannel();

        newFormSetup();

        setMenu();

        setBottomNavigationViewMain();

    }

    private void newFormSetup() {
        FloatingActionButton floatingActionButton = findViewById(R.id.newFormButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormSetup.class);
            startActivityForResult(intent, REQUEST_FORM_SETUP);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FORM_SETUP) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Vehicle vehicle = (Vehicle) data.getExtras().get("Vehicle");
                vehicleViewModel.insert(vehicle);
                setNotificationTime(vehicle);
            }
        }
    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Channel For Service Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID_FOR_NOTIFICATION_CHANNEL, NAME_FOR_NOTIFICATION_CHANNEL, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigationViewMain() {
        BottomNavigationView bottomNavigationViewMain = findViewById(R.id.bottomNavigationMain);
        bottomNavigationViewMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mainPanel:
                    openFragment(new HomeScreenFragments());
                    return true;
                case R.id.favoritesPanel:
                    openFragment(new FavoritesScreenFragment());
                    return true;
                case R.id.upcomingServices:
                    openFragment(new UpcomingServicesScreenFragment());
                    return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void settingsInit() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.settingsFragment, new SettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setNotificationTime(Vehicle vehicle) {
        Intent intent = new Intent(MainActivity.this, ServiceNotification.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Vehicle", vehicle);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long testingTimeToNotify = System.currentTimeMillis() + 1000 * 5;

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                testingTimeToNotify,
                pendingIntent);
    }

    private void setMenu() {
        Toolbar toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawerMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }
}