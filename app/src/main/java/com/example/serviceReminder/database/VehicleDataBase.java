package com.example.serviceReminder.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vehicle.class}, version = 9, exportSchema = false)
public abstract class VehicleDataBase extends RoomDatabase {
    public abstract VehicleDao vehicleDao();

    private static VehicleDataBase INSTANCE;

    public static synchronized VehicleDataBase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    VehicleDataBase.class, "Vehicle")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
