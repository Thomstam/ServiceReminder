package com.example.serviceReminder.drawerMainContents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.serviceReminder.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditProfilePreferences extends AppCompatActivity {

    private ImageView profilePicture;
    private EditText companyName;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_PHOTO_FOR_AVATAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_preferences);

        setCompleteSettings();

        verifyStoragePermissions(this);

        setProfilePicture();

        loadProfilePicture();

        setCompanyName();

        loadCompanyName();
    }

    private void setCompleteSettings(){
        ImageButton completeSettings = findViewById(R.id.completeFormProfileSettings);
        completeSettings.setOnClickListener(v -> {
            if (companyName.getText().toString().equals("")){
                companyName.setError("Cannot Be Empty");
                return;
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("companyName", companyName.getText().toString());
            editor.apply();
            setResult(RESULT_OK);
            finish();
        });
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

    private void setProfilePicture(){
        profilePicture = findViewById(R.id.ProfilePictureEditForm);
        profilePicture.setOnClickListener(v -> pickImage());
    }

    private void setCompanyName(){
        companyName = findViewById(R.id.companyNameEditForm);
    }

    private void loadCompanyName(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("companyName", null) != null){
            companyName.setText(preferences.getString("companyName", null));
        }
    }

    public void loadProfilePicture() {
        File root = Environment.getExternalStorageDirectory();
        String pathFile = root + "/saved_images/" + "profilePhoto";
        Bitmap bMap = BitmapFactory.decodeFile(pathFile);
        if (bMap != null) {
            bMap = getCroppedBitmap(bMap);
            profilePicture.setImageBitmap(bMap);
        }
    }

    public void pickImage() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
        }else {
            verifyStoragePermissions(this);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 140, 140, true);
                resized = getCroppedBitmap(resized);
                saveTempBitmap(resized);
                loadProfilePicture();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2f, bitmap.getHeight() / 2f,
                bitmap.getWidth() / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            bitmapToFile(bitmap);
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void bitmapToFile(Bitmap bitmap) {
        String fileName = "profilePhoto";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}