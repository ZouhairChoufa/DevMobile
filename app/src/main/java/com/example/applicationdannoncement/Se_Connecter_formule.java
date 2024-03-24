package com.example.applicationdannoncement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Se_Connecter_formule extends AppCompatActivity{
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewError;
    private Button buttonLogin;
    private TextView textViewSignUpLink;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.SignInBtn);
        textViewSignUpLink = findViewById(R.id.textViewSignUpLink);
        textViewError = findViewById(R.id.textViewError);

        textViewError.setVisibility(View.GONE);

        database = openOrCreateDatabase("Utilisateur.db", MODE_PRIVATE, null);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Se_Connecter_formule.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = database.rawQuery("SELECT * FROM utilisateur WHERE email = ? AND password = ?", new String[]{email, password});

                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(Se_Connecter_formule.this, Interface3_Formulaire.class);
                    startActivity(intent);
                    finish();
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                }

                cursor.close();
            }
        });

        textViewSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Se_Connecter_formule.this, Forme_Cree_Compte.class);
                startActivity(intent);
            }
        });
    }
}