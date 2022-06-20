package com.example.youtravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class MyTripsActivity extends AppCompatActivity {

    private TimelineItemRecyclerViewAdapter adapter;

    private final ArrayList<TimelineItemModel> timelineItemModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        Log.d("TAG", id);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FirebaseFirestore.getInstance().collection(id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }
}