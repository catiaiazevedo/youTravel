package com.example.youtravel;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> frontImages;
    List<String> names;
    List<String> secondImages;
    List<String> tagLines;
    List<String> price;
    List<String> description;
    ImageView firstImage, secondImage, thirdImage, fourthImage, fifthImage, sixthImage, seventhImage;
    TextView firstText, secondText, thirdText, fourthText, fifthText, sixthText, seventhText;

    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("categories").document("todos");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    names = (List<String>) document.get("name");
                    if (document.exists())
                    {
                        frontImages = (List<String>) document.get("front_image");

                        firstImage = findViewById(R.id.firstImage);
                        secondImage = findViewById(R.id.secondImage);
                        thirdImage = findViewById(R.id.thirdImage);
                        fourthImage = findViewById(R.id.fourthImage);
                        fifthImage = findViewById(R.id.fifthImage);
                        sixthImage = findViewById(R.id.sixthImage);
                        seventhImage = findViewById(R.id.seventhImage);

                        Picasso
                                .get()
                                .load(frontImages.get(0))
                                .into(firstImage);

                        Picasso
                                .get()
                                .load(frontImages.get(1))
                                .into(secondImage);

                        Picasso
                                .get()
                                .load(frontImages.get(2))
                                .into(thirdImage);

                        Picasso
                                .get()
                                .load(frontImages.get(3))
                                .into(fourthImage);

                        Picasso
                                .get()
                                .load(frontImages.get(4))
                                .into(fifthImage);

                        Picasso
                                .get()
                                .load(frontImages.get(5))
                                .into(sixthImage);

                        Picasso
                                .get()
                                .load(frontImages.get(6))
                                .into(seventhImage);

                        firstText = findViewById(R.id.firstText);
                        secondText = findViewById(R.id.secondText);
                        thirdText = findViewById(R.id.thirdText);
                        fourthText = findViewById(R.id.fourthText);
                        fifthText = findViewById(R.id.fifthText);
                        sixthText = findViewById(R.id.sixthText);
                        seventhText = findViewById(R.id.seventhText);

                        firstText.setText(names.get(0));
                        secondText.setText(names.get(1));
                        thirdText.setText(names.get(2));
                        fourthText.setText(names.get(3));
                        fifthText.setText(names.get(4));
                        sixthText.setText(names.get(5));
                        seventhText.setText(names.get(6));

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, names);
                        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.searchBar);
                        textView.setAdapter(adapter);

                        textView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(MainActivity.this, "get failed with "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void locationOnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Location.class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("categories").document("todos");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    names = (List<String>) document.get("name");
                    secondImages = (List<String>) document.get("second_image");
                    tagLines = (List<String>) document.get("tagLine");
                    price = (List<String>) document.get("price");
                    description = (List<String>) document.get("description");

                    if (document.exists())
                    {
                        switch (view.getId())
                        {
                            case R.id.firstImage:
                                intent.putExtra("name", names.get(0));
                                intent.putExtra("second_image", secondImages.get(0));
                                intent.putExtra("tagLine", tagLines.get(0));
                                intent.putExtra("price", price.get(0));
                                intent.putExtra("description", description.get(0));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.secondImage:
                                intent.putExtra("name", names.get(1));
                                intent.putExtra("second_image", secondImages.get(1));
                                intent.putExtra("tagLine", tagLines.get(1));
                                intent.putExtra("price", price.get(1));
                                intent.putExtra("description", description.get(1));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.thirdImage:
                                intent.putExtra("name", names.get(2));
                                intent.putExtra("second_image", secondImages.get(2));
                                intent.putExtra("tagLine", tagLines.get(2));
                                intent.putExtra("price", price.get(2));
                                intent.putExtra("description", description.get(2));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.fourthImage:
                                intent.putExtra("name", names.get(3));
                                intent.putExtra("second_image", secondImages.get(3));
                                intent.putExtra("tagLine", tagLines.get(3));
                                intent.putExtra("price", price.get(3));
                                intent.putExtra("description", description.get(3));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.fifthImage:
                                intent.putExtra("name", names.get(4));
                                intent.putExtra("second_image", secondImages.get(4));
                                intent.putExtra("tagLine", tagLines.get(4));
                                intent.putExtra("price", price.get(4));
                                intent.putExtra("description", description.get(4));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.sixthImage:
                                intent.putExtra("name", names.get(5));
                                intent.putExtra("second_image", secondImages.get(5));
                                intent.putExtra("tagLine", tagLines.get(5));
                                intent.putExtra("price", price.get(5));
                                intent.putExtra("description", description.get(5));
                                startActivity(intent);
                                onPause();
                            break;
                            case R.id.seventhImage:
                                intent.putExtra("name", names.get(6));
                                intent.putExtra("second_image", secondImages.get(6));
                                intent.putExtra("tagLine", tagLines.get(6));
                                intent.putExtra("price", price.get(6));
                                intent.putExtra("description", description.get(6));
                                startActivity(intent);
                                onPause();
                            break;
                        }
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(MainActivity.this, "get failed with "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logoutUser()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        onPause();
        finish();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addTrip:
                        return true;

                    case R.id.myTrips:
                        return true;

                    case R.id.logout:
                        logoutUser();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}