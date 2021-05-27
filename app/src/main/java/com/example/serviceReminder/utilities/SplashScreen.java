package com.example.serviceReminder.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serviceReminder.R;
import com.example.serviceReminder.mainFragments.MainActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        TextView service = findViewById(R.id.textService);
        TextView reminder = findViewById(R.id.textReminder);
        runAnimation(service);
        runAnimation(reminder);


        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
        }, 2000);
    }

    private void runAnimation(View view){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        view.startAnimation(animation);
    }
}