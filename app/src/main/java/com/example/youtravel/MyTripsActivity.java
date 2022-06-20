package com.example.youtravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class MyTripsActivity extends AppCompatActivity {

    private TimelineItemRecyclerViewAdapter adapter;

    private final ArrayList<TimelineItemModel> timelineItemModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        setActionBar();

        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FirebaseFirestore.getInstance().collection(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            timelineItemModels.add(new TimelineItemModel(document.getString("URL"),
                                    document.getString("Location"),
                                    document.getString("Date"),
                                    document.getString("Rating")));
                        }
                        Collections.sort(timelineItemModels);
                        Collections.reverse(timelineItemModels);
                        adapter = new TimelineItemRecyclerViewAdapter(MyTripsActivity.this,timelineItemModels);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyTripsActivity.this));
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_trip, menu);

        // first parameter is the file for icon and second one is menu
        return super.onCreateOptionsMenu(menu);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_TITLE);

        actionBar.setCustomView(R.layout.abs_layout);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8a2c8")));

        TextView titleView = findViewById(R.id.absLayout);
        titleView.setText("Travel Journal");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (item.getItemId() == R.id.addButton){
            startActivity(new Intent(MyTripsActivity.this, AddTripActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}