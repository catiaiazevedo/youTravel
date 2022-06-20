package com.example.youtravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Explore extends AppCompatActivity {

    TextView name, description, textQR;
    Button qrCode, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("name");
        String text = bundle.getString("description");
        String latitude = bundle.getString("latitude");
        String longitude = bundle.getString("longitude");

        location = findViewById(R.id.location);
        location.setOnClickListener(view -> {
            Intent intent = new Intent(Explore.this,MapActivity.class);
            intent.putExtra("name", title);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        });
        name = findViewById(R.id.name);
        name.setText(title);

        description = findViewById(R.id.description);
        description.setText(text);

        textQR = findViewById(R.id.textQR);
        textQR.setText("Never visited "+title+"? Get our QR code that will give you 15% discount in selected hotels, restaurants and museums all over the city!");

        qrCode = findViewById(R.id.qrCode);
        qrCode.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Explore.this);
            alertDialog.setTitle("Generated QR Code");

            final ImageView imgqr = new ImageView(Explore.this);

            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(title, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                imgqr.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            LinearLayout ll = new LinearLayout(Explore.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(imgqr);
            alertDialog.setView(ll);

            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Saved", (dialog, id) -> {

            });

            AlertDialog alert = alertDialog.create();
            alert.show();
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