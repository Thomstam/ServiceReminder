package com.example.serviceReminder.database;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.serviceReminder.utilities.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VehicleRepository {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final VehicleDao vehicleDao;
    private final LiveData<List<Vehicle>> startScreenVehicles;
    private final LiveData<List<Vehicle>> favoritesScreenVehicle;
    private final LiveData<List<Vehicle>> upComingServices;

    public VehicleRepository(Application application) {
        VehicleDataBase vehicleDataBase = VehicleDataBase.getINSTANCE(application);
        vehicleDao = vehicleDataBase.vehicleDao();
        startScreenVehicles = vehicleDao.getAllVehicles();
        favoritesScreenVehicle = vehicleDao.favoritesList();
        upComingServices = vehicleDao.upComingServices();
    }

    public LiveData<List<Vehicle>> getStartScreenVehicles() {
        return startScreenVehicles;
    }

    public LiveData<List<Vehicle>> getFavoritesScreenVehicle() {
        return favoritesScreenVehicle;
    }

    public LiveData<List<Vehicle>> getUpComingServices() {
        return upComingServices;
    }

    public void insert(Vehicle vehicle) {
        insertNewVehicle(vehicle);
    }

    public void delete(Vehicle vehicle) {
        deleteVehicle(vehicle);
    }

    public void update(Vehicle vehicle) {
        updateVehicle(vehicle);
    }

    public void updateAllVehicles(List<Vehicle> vehicles) {
        updateVehicleKms(vehicles);
    }

    public void deleteAllVehicles(){
        nukeTable();
    }

    private void nukeTable(){
        new Thread(vehicleDao::nukeTable).start();
    }

    private void insertNewVehicle(Vehicle vehicle) {
        new Thread(() -> vehicleDao.insertVehicle(vehicle)).start();
    }

    private void deleteVehicle(Vehicle vehicle) {
        new Thread(() -> vehicleDao.deleteVehicle(vehicle)).start();
    }

    private void updateVehicle(Vehicle vehicle) {
        new Thread(() -> vehicleDao.update(vehicle)).start();
    }

    private void updateVehicleKms(List<Vehicle> vehicles) {
        new Thread(() -> {
            for (Vehicle vehicle : vehicles) {
                try {
                    Date test1 = sdf.parse(sdf.format(vehicle.getLastTimeKmsUpdated()));
                    Date test2 = sdf.parse(sdf.format(System.currentTimeMillis()));
                    assert test1 != null;
                    assert test2 != null;
                    if (test1.getTime() != test2.getTime()) {
                        int daysPassed = (int) (test2.getTime() - test1.getTime() / 86400000);
                        int newKms = daysPassed * (vehicle.getKmsPerDay() * vehicle.getUsagePerWeek() / 7);
                        vehicle.setCurrentKms(newKms);
                        vehicle.setLastTimeKmsUpdated(System.currentTimeMillis());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
