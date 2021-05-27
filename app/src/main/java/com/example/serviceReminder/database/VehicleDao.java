package com.example.serviceReminder.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VehicleDao {

    @Query("SELECT * FROM vehicle ORDER BY id DESC")
    LiveData<List<Vehicle>> getAllVehicles();

    @Query("SELECT * FROM vehicle WHERE isFavorite = 1 ORDER BY id DESC")
    LiveData<List<Vehicle>> favoritesList();

    @Query("SELECT * FROM vehicle ORDER BY notificationTime ASC")
    LiveData<List<Vehicle>> upComingServices();

    @Query("DELETE FROM vehicle")
    void nukeTable();

    @Insert
    void insertVehicle(Vehicle vehicle);

    @Delete
    void deleteVehicle(Vehicle vehicle);

    @Update
    void update(Vehicle vehicle);

}
