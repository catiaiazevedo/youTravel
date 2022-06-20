package com.example.youtravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
        }

        login = findViewById(R.id.login);
        login.setOnClickListener(view -> loginUser());

        register = findViewById(R.id.register);
        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            onPause();
            finish();
        });
    }

    private void loginUser()
    {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        password.setTransformationMethod(new PasswordTransformationMethod());

        String mail = email.getText().toString();
        String pass = password.getText().toString();

        if (mail.isEmpty() || pass.isEmpty())
            Toast.makeText(this, "Preencha todos os campos acima!", Toast.LENGTH_LONG).show();
        else
        {
            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            onPause();
                            finish();
                        }

                        else
                        {
                            Toast.makeText(LoginActivity.this, "O seu Inicio de Sessão falhou!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
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