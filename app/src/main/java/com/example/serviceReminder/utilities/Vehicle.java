package com.example.serviceReminder.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity
        (indices = {@Index(value = {"platesOfVehicle"},
                unique = true)})
public class Vehicle implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String platesOfVehicle;
    private String typeOfVehicle;
    private int brandIcon;
    private int brandIconSpinnerPosition;
    private int currentKms;
    private int serviceKms;
    private int kmsPerDay;
    private int usagePerWeek;
    private long notificationTime;
    private int notificationSpinnerTimeSelection;
    private String dateOfTheService;
    private boolean isFavorite;
    private long lastTimeKmsUpdated;
    private String hourAndMinOfTheNotification;


    public Vehicle(@NonNull String platesOfVehicle, int brandIcon, int brandIconSpinnerPosition, String typeOfVehicle, int currentKms, int serviceKms, int kmsPerDay, int usagePerWeek, long notificationTime, int notificationSpinnerTimeSelection, String dateOfTheService, boolean isFavorite, long lastTimeKmsUpdated, String hourAndMinOfTheNotification) {
        this.platesOfVehicle = platesOfVehicle;
        this.brandIcon = brandIcon;
        this.brandIconSpinnerPosition = brandIconSpinnerPosition;
        this.typeOfVehicle = typeOfVehicle;
        this.currentKms = currentKms;
        this.serviceKms = serviceKms;
        this.kmsPerDay = kmsPerDay;
        this.usagePerWeek = usagePerWeek;
        this.notificationTime = notificationTime;
        this.notificationSpinnerTimeSelection = notificationSpinnerTimeSelection;
        this.dateOfTheService = dateOfTheService;
        this.isFavorite = isFavorite;
        this.lastTimeKmsUpdated = lastTimeKmsUpdated;
        this.hourAndMinOfTheNotification = hourAndMinOfTheNotification;
    }

    protected Vehicle(Parcel in) {
        id = in.readInt();
        platesOfVehicle = in.readString();
        typeOfVehicle = in.readString();
        brandIcon = in.readInt();
        brandIconSpinnerPosition = in.readInt();
        currentKms = in.readInt();
        serviceKms = in.readInt();
        kmsPerDay = in.readInt();
        usagePerWeek = in.readInt();
        notificationTime = in.readLong();
        notificationSpinnerTimeSelection = in.readInt();
        dateOfTheService = in.readString();
        isFavorite = in.readInt() == 0;
        lastTimeKmsUpdated = in.readLong();
        hourAndMinOfTheNotification = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(platesOfVehicle);
        dest.writeString(typeOfVehicle);
        dest.writeInt(brandIcon);
        dest.writeInt(brandIconSpinnerPosition);
        dest.writeInt(currentKms);
        dest.writeInt(serviceKms);
        dest.writeInt(kmsPerDay);
        dest.writeInt(usagePerWeek);
        dest.writeLong(notificationTime);
        dest.writeInt(notificationSpinnerTimeSelection);
        dest.writeString(dateOfTheService);
        dest.writeInt(isFavorite ? 0 : 1);
        dest.writeLong(lastTimeKmsUpdated);
        dest.writeString(hourAndMinOfTheNotification);
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    public String getPlatesOfVehicle() {
        return platesOfVehicle;
    }

    public void setPlatesOfVehicle(@NonNull String platesOfVehicle) {
        this.platesOfVehicle = platesOfVehicle;
    }

    public String getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(String typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public int getCurrentKms() {
        return currentKms;
    }

    public void setCurrentKms(int currentKms) {
        this.currentKms = currentKms;
    }

    public int getServiceKms() {
        return serviceKms;
    }

    public void setServiceKms(int serviceKms) {
        this.serviceKms = serviceKms;
    }

    public int getKmsPerDay() {
        return kmsPerDay;
    }

    public void setKmsPerDay(int kmsPerDay) {
        this.kmsPerDay = kmsPerDay;
    }

    public int getUsagePerWeek() {
        return usagePerWeek;
    }

    public void setUsagePerWeek(int usagePerWeek) {
        this.usagePerWeek = usagePerWeek;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    public int getNotificationSpinnerTimeSelection() {
        return notificationSpinnerTimeSelection;
    }

    public void setNotificationSpinnerTimeSelection(int notificationSpinnerTimeSelection) {
        this.notificationSpinnerTimeSelection = notificationSpinnerTimeSelection;
    }

    public int getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(int brandIcon) {
        this.brandIcon = brandIcon;
    }

    public String getDateOfTheService() {
        return dateOfTheService;
    }

    public void setDateOfTheService(String dateOfTheService) {
        this.dateOfTheService = dateOfTheService;
    }

    public int getBrandIconSpinnerPosition() {
        return brandIconSpinnerPosition;
    }

    public void setBrandIconSpinnerPosition(int brandIconSpinnerPosition) {
        this.brandIconSpinnerPosition = brandIconSpinnerPosition;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastTimeKmsUpdated() {
        return lastTimeKmsUpdated;
    }

    public void setLastTimeKmsUpdated(long lastTimeKmsUpdated) {
        this.lastTimeKmsUpdated = lastTimeKmsUpdated;
    }

    public String getHourAndMinOfTheNotification() {
        return hourAndMinOfTheNotification;
    }

    public void setHourAndMinOfTheNotification(String hourAndMinOfTheNotification) {
        this.hourAndMinOfTheNotification = hourAndMinOfTheNotification;
    }
}
