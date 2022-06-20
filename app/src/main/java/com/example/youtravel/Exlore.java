package com.example.youtravel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Exlore extends AppCompatActivity {

    TextView name, description, textQR;
    Button qrCode, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exlore);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("name");
        String text = bundle.getString("description");
        String prices = bundle.getString("price");
        String latitude = bundle.getString("latitude");
        String longitude = bundle.getString("longitude");

        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //JUNQUEIRA AQUI
                System.out.println(latitude);
                System.out.println(longitude);
            }
        });

        name = findViewById(R.id.name);
        name.setText(title);

        description = findViewById(R.id.description);
        description.setText(text);

        textQR = findViewById(R.id.textQR);
        textQR.setText("Never visited "+title+"? Get our QR code that will give you 15% discount in selected hotels, restaurants and museums all over the city!");

        qrCode = findViewById(R.id.qrCode);
        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Exlore.this);
                alertDialog.setTitle("Generated QR Code");

                final ImageView imgqr = new ImageView(Exlore.this);

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

                LinearLayout ll = new LinearLayout(Exlore.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(imgqr);
                alertDialog.setView(ll);

                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Saved",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();
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