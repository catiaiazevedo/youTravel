package com.example.youtravel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity {

    public static final int LOCATION_PERM_CODE = 100;
    private static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;


    private Uri contentUri;
    ImageView selectedImage;
    Button cameraBtn, saveBtn;
    RatingBar ratingBar;
    FirebaseStorage storage;
    StorageReference storageReference;
    FusedLocationProviderClient fusedLocationProviderClient;
    String localityName, imageData, currentPhotoPath, id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        saveBtn = findViewById(R.id.saveBtn);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);
        saveBtn.setEnabled(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cameraBtn.setOnClickListener(view -> askCameraPermissions());


        saveBtn.setOnClickListener(view -> saveInFirebase());
    }


    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERM_CODE);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null){
                try {
                    Geocoder geocoder = new Geocoder(AddTripActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    localityName = addresses.get(0).getLocality();
                    imageData = LocalDate.now().toString();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveInFirebase() {

        if (contentUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            StorageReference reference = storageReference.child(id+"/" + UUID.randomUUID().toString());
            reference.putFile(contentUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> image = new HashMap<>();
                    image.put("URL", uri.toString());
                    image.put("Location", localityName);
                    image.put("Date", imageData);
                    image.put("Rating", String.valueOf(ratingBar.getRating()));
                    db.collection(id).add(image);
                });
                saveBtn.setEnabled(false);
                ratingBar.setEnabled(false);
            })
                    .addOnFailureListener(e -> Toast.makeText(AddTripActivity.this, "Error occurred" + e.getMessage(), Toast.LENGTH_SHORT).show())
                    . addOnProgressListener(snapshot -> {
                        double progress = (100.0 * (double)snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Saved " + (int) progress + "%");

                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                contentUri = Uri.fromFile(f);
                selectedImage.setImageURI(contentUri);
                saveBtn.setEnabled(true);
                ratingBar.setEnabled(true);
                getLocation();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.youtravel.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}