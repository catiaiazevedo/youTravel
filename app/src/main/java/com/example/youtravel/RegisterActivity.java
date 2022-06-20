package com.example.youtravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
        }

        register = findViewById(R.id.register);
        register.setOnClickListener(view -> registerUser());

        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            onPause();
            finish();
        });
    }

    private void registerUser()
    {
        EditText firstname = findViewById(R.id.firstname);
        EditText lastname = findViewById(R.id.lastname);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        password.setTransformationMethod(new PasswordTransformationMethod());

        String nome = firstname.getText().toString();
        String apelido = lastname.getText().toString();
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        if (nome.isEmpty() || apelido.isEmpty() || mail.isEmpty() || pass.isEmpty())
            Toast.makeText(this, "Preencha todos os campos acima!", Toast.LENGTH_LONG).show();

        if (pass.length()<6)
            Toast.makeText(this, "A sua palavra passe deverá ter pelos menos 6 caracteres!", Toast.LENGTH_LONG).show();

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = new User(nome, apelido, mail);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    onPause();
                                    finish();
                                })
                                .addOnFailureListener(e -> {

                                });

                        /*FirebaseDatabase.getInstance("https://youlearnicm-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });*/
                    } else {
                        Toast.makeText(RegisterActivity.this, "O seu Registo falhou!", Toast.LENGTH_LONG).show();
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