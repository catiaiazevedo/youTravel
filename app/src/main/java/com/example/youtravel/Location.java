package com.example.youtravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Location extends AppCompatActivity {

    TextView title, tag, price;
    Button back, explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String image = bundle.getString("second_image");
        String tagLine = bundle.getString("tagLine");
        String prices = bundle.getString("price");
        String description = bundle.getString("description");
        String latitude = bundle.getString("latitude");
        String longitude = bundle.getString("longitude");

        explore = findViewById(R.id.explore);
        explore.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Explore.class);
            intent.putExtra("name", name);
            intent.putExtra("description", description);
            intent.putExtra("price", prices);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            onPause();
        });

        LinearLayout layout =(LinearLayout) findViewById(R.id.layout);

        Picasso
                .get()
                .load(image)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        layout.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        title = findViewById(R.id.name);
        title.setText(name);

        tag = findViewById(R.id.tag);
        tag.setText(tagLine);

        price = findViewById(R.id.price);
        price.setText("since "+prices);
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