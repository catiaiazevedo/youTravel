package com.example.youtravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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
}