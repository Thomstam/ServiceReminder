package com.example.serviceReminder.drawerMainContents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

import com.example.serviceReminder.R;

public class DrawerHeaderFragment extends PreferenceFragmentCompat {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(com.example.serviceReminder.R.xml.drawer_heard_main_preferences, rootKey);

        NavigationHeaderPreferences navigationHeaderPreferences = findPreference("profile_picture");
        verifyStoragePermissions(getActivity());

        assert navigationHeaderPreferences != null;

        navigationHeaderPreferences.setEditClickListener(v -> {
            openEditPanel();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_header_profile_settings, new DrawerHeaderFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    private void openEditPanel() {
        Intent profileSettingsIntent = new Intent(getActivity(), EditProfilePreferences.class);
        startActivityForResult(profileSettingsIntent, 1);

    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
