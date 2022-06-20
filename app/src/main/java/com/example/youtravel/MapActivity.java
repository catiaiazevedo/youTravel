package com.example.youtravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setActionBar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String latitude = bundle.getString("latitude");
        String longitude = bundle.getString("longitude");
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        Log.d("TAG", id);
        map = googleMap;

        FirebaseFirestore.getInstance().collection(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Objects.requireNonNull(document.getString("Latitude"))),
                                    Double.parseDouble(Objects.requireNonNull(document.getString("Longitude")))))
                            .title("Marker in "+document.getString("Location"))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
        }
    });
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker in " +name));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_TITLE);

        actionBar.setCustomView(R.layout.abs_layout);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8a2c8")));

        TextView titleView = findViewById(R.id.absLayout);
        titleView.setText("Travel Journal Map");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}