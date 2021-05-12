package com.example.serviceReminder.drawerMainContents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.serviceReminder.R;
import com.example.serviceReminder.database.VehicleViewModel;
import com.example.serviceReminder.mainFragments.MainActivity;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    private int mSelected = -1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setLanguageInit();

        addPreferencesFromResource(R.xml.root_preferences);

        setLanguage();

        setSoundPicker();

        deleteDB();
    }

    private void setLanguageInit(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Locale locale = new Locale(preferences.getString("language",""));
        Locale.setDefault(locale);
        Configuration configuration = requireContext().getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        requireContext().getResources().updateConfiguration(configuration, requireContext().getResources().getDisplayMetrics());
    }

    private void deleteDB(){
        Preference preference = findPreference("deleteDB");
        assert preference != null;
        preference.setOnPreferenceClickListener(preference1 -> {
            VehicleViewModel vehicleViewModel = new ViewModelProvider(requireActivity()).get(VehicleViewModel.class);
            new AlertDialog.Builder(getContext())
                    .setTitle("DELETE ALL VEHICLES")
                    .setMessage("CAUTION: Are you sure you want to delete all the vehicles?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> vehicleViewModel.nukeTable())
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        });
    }

    private void setLanguage(){
        ListPreference preference = findPreference("language");
        assert preference != null;
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            setLanguageSelection(newValue.toString());
            preference.setValue(newValue.toString());
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
            return false;
        });
    }

    private void setLanguageSelection(String selectedLanguage){
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        requireContext().getResources().updateConfiguration(configuration, requireActivity().getBaseContext().getResources().getDisplayMetrics());
    }


    private void setSoundPicker() {
        Preference preference = findPreference("notificationSounds");
        assert preference != null;
        final int[] sounds = {R.raw.got_it_done, R.raw.hasty_ba_dum_tss};
        final String[] soundsName = {"Got It Done", "Hasty Ba Dum Tss"};
        preference.setOnPreferenceClickListener(preference1 -> {


            AlertDialog.Builder build = new AlertDialog.Builder(getContext());
            build.setTitle("Choose Notification Sound");
            build.setCancelable(true);
            mSelected = -1;

            final DialogInterface.OnMultiChoiceClickListener onClick =
                    (dialog, which, isChecked) -> {

                        if (isChecked) {
                            if ((mSelected != -1) && (mSelected != which)) {
                                final int oldVal = mSelected;
                                final AlertDialog alert = (AlertDialog) dialog;
                                final ListView list = alert.getListView();
                                list.setItemChecked(oldVal, false);
                            }
                            mSelected = which;
                            playSound(sounds[which]);

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("SoundPreference", soundsName[which]);
                            editor.apply();

                            new MainActivity().notificationChannel(getContext());
                        } else
                            mSelected = -1;
                    };
            build.setMultiChoiceItems(soundsName, null, onClick);
            build.setPositiveButton("Done", (dialog, which) -> {
                if (mSelected == -1) {
                    Toast.makeText(getContext(), "You didn't select anything.", Toast.LENGTH_SHORT).show();
                }
            });
            build.setNegativeButton("Cancel", null);
            build.show();
            return false;
        });
    }

    private void playSound(int soundTrack) {
        MediaPlayer mPlayer = MediaPlayer.create(getActivity(), soundTrack);
        mPlayer.start();
    }
}

