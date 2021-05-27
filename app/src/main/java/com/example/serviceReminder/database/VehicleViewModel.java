package com.example.serviceReminder.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VehicleViewModel extends AndroidViewModel {

    private final VehicleRepository vehicleRepository;
    private final LiveData<List<Vehicle>> startScreenVehicles;
    private final LiveData<List<Vehicle>> favoritesScreenVehicles;
    private final LiveData<List<Vehicle>> upComingServices;

    public VehicleViewModel(@NonNull Application application) {
        super(application);
        vehicleRepository = new VehicleRepository(application);
        startScreenVehicles = vehicleRepository.getStartScreenVehicles();
        favoritesScreenVehicles = vehicleRepository.getFavoritesScreenVehicle();
        upComingServices = vehicleRepository.getUpComingServices();
    }

    public LiveData<List<Vehicle>> getStartScreenVehicles() {
        return startScreenVehicles;
    }

    public LiveData<List<Vehicle>> getFavoritesScreenVehicles() {
        return favoritesScreenVehicles;
    }

    public LiveData<List<Vehicle>> getUpComingServices() {
        return upComingServices;
    }


    public void updateVehicle(List<Vehicle> vehicles) {
        vehicleRepository.updateAllVehicles(vehicles);
    }

    public void nukeTable() {
        vehicleRepository.deleteAllVehicles();
    }

    public void insert(Vehicle vehicle) {
        vehicleRepository.insert(vehicle);
    }

    public void update(Vehicle vehicle) {
        vehicleRepository.update(vehicle);
    }

    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }
    
}
