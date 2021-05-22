package com.example.serviceReminder.drawerMainContents;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import com.example.serviceReminder.R;

import java.io.File;

public class NavigationHeaderPreferences extends Preference {


    View.OnClickListener editClickListener;
    private TextView companyName;
    private ImageView profilePicture;

    public NavigationHeaderPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        profilePicture = (ImageView) holder.findViewById(R.id.ProfilePicture);
        companyName = (TextView) holder.findViewById(R.id.companyNameSetText);
        ImageButton editProfile = (ImageButton) holder.findViewById(R.id.navigationHeaderEditButton);
        editProfile.setOnClickListener(editClickListener);
        setCompanyName();
        loadProfilePicture();
    }


    public void setEditClickListener(View.OnClickListener onClickListener) {
        editClickListener = onClickListener;
    }

    public void setCompanyName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        companyName.setText(preferences.getString("companyName", ""));
    }


    public void loadProfilePicture() {
        File root = Environment.getExternalStorageDirectory();
        String pathFile = root + "/saved_images/" + "profilePhoto";
        Bitmap bMap = BitmapFactory.decodeFile(pathFile);
        if (bMap != null) {
            bMap = EditProfilePreferences.getCroppedBitmap(bMap);
            profilePicture.setImageBitmap(bMap);
        }
    }
}
