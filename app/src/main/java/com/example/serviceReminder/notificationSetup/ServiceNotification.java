package com.example.serviceReminder.notificationSetup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.example.serviceReminder.R;
import com.example.serviceReminder.mainFragments.MainActivity;
import com.example.serviceReminder.database.Vehicle;

import java.util.concurrent.atomic.AtomicInteger;

public class ServiceNotification extends BroadcastReceiver {

    private final static String ID_FOR_NOTIFICATION_CHANNEL = "ServiceReminder";
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder builder;
    private Color lights;
    private final static String NAME_FOR_NOTIFICATION_CHANNEL = "DefaultNotificationChannel";


    @Override
    public void onReceive(Context context, Intent intent) {

        clickToOpenMain(context);

        Bundle bundle = intent.getBundleExtra("bundle");
        Vehicle vehicle = bundle.getParcelable("Vehicle");

        setPreferences(context);

        setBuilder(vehicle, context);

        notificationChannel(context);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NotificationID.getID(), builder.build());
    }

    private void clickToOpenMain(Context context) {
        Intent intentToOpenMain = new Intent(context, MainActivity.class);
        intentToOpenMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intentToOpenMain, 0);
    }

    private void setBuilder(Vehicle vehicle, Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_colapsed);
        setNotificationLayout(remoteViews, vehicle);
        builder = new NotificationCompat.Builder(context, ID_FOR_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(remoteViews)
                .setLights(lights.toArgb(), lights.toArgb(), lights.toArgb())
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private void setPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString("notificationLightsList", "Red").equals("Red")) {
            lights = Color.valueOf(Color.RED);
        } else if (preferences.getString("notificationLightsList", "Red").equals("Blue")) {
            lights = Color.valueOf(Color.BLUE);
        } else {
            lights = Color.valueOf(Color.WHITE);
        }
    }

    private void setNotificationLayout(RemoteViews remoteViews, Vehicle vehicle) {
        remoteViews.setTextViewText(R.id.platesOfVehicleNotification, vehicle.getPlatesOfVehicle());
        remoteViews.setImageViewResource(R.id.brandNotificationIcon, vehicle.getBrandIcon());
    }

    public void notificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int sound = soundID(context);
            String description = "Channel For Service Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID_FOR_NOTIFICATION_CHANNEL, NAME_FOR_NOTIFICATION_CHANNEL, importance);
            channel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            channel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + sound), audioAttributes);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int soundID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString("SoundPreference", "").equals("Got It Done")) {
            return R.raw.got_it_done;
        } else {
            return R.raw.hasty_ba_dum_tss;
        }
    }


    private static class NotificationID {

        private final static AtomicInteger c = new AtomicInteger(0);

        public static int getID() {
            return c.incrementAndGet();
        }


    }

}
