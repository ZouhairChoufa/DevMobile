package com.example.applicationdannoncement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Forme_Cree_Compte extends AppCompatActivity {

    private static final String DATABASE_NAME = "Utilisateur.db";
    private static final int DATABASE_VERSION = 1;

    private static class UserContract {
        private UserContract() {}

        public static class UserEntry {
            public static final String _ID = "id";
            public static final String TABLE_NAME = "utilisateur";
            public static final String COLUMN_EMAIL = "email";
            public static final String COLUMN_PASSWORD = "password";
            public static final String COLUMN_VOUS_ETES = "vous_etes";
            public static final String COLUMN_VILLE = "ville";

        }
    }

    private static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                    UserContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    UserContract.UserEntry.COLUMN_VOUS_ETES + " TEXT NOT NULL, " +
                    UserContract.UserEntry.COLUMN_VILLE + " TEXT NOT NULL" +
                    ");";

            db.execSQL(SQL_CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    private DbHelper dbHelper;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forme_cree_compte);

        dbHelper = new DbHelper(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPass = findViewById(R.id.editTextConfirmPassword);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(v -> signUp());

        Spinner spinnerCity = findViewById(R.id.spinnerCity);
        List<String> morocainVille = Arrays.asList(
                "Agadir", "Al Hoceima", "Assilah", "Azemmour", "Azrou",
                "Beni Mellal", "Bouznika", "Casablanca", "Chefchaouen",
                "Dakhla", "El Jadida", "Errachidia", "Essaouira", "Fes",
                "Fnideq", "Guelmim", "Ifrane", "Kénitra", "Khouribga",
                "Laayoune", "Larache", "Marrakech", "Meknes", "Mohammedia",
                "Nador", "Ouarzazate", "Oujda", "Rabat", "Safi", "Salé",
                "Sefrou", "Settat", "Sidi Ifni", "Tangier", "Tan-Tan",
                "Taroudant", "Taza", "Tétouan", "Tiznit", "Zagora");
        Collections.sort(morocainVille);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, morocainVille);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
    }

    private void signUp() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String vousEtes = getVousEtes();
        String confirmePass = editTextConfirmPass.getText().toString();
        String ville = getSelectedCity();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkEmailExists(email)) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!confirmePass.equals(password)){
            Toast.makeText(this, "Second Password must be the same pass like the first one", Toast.LENGTH_SHORT).show();
            return;
        }

        insertUserData(email, password, vousEtes, ville);
        Toast.makeText(this, "User signed up successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + UserContract.UserEntry.TABLE_NAME + " WHERE " +
                UserContract.UserEntry.COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private String getVousEtes() {
        RadioGroup radioGroup = findViewById(R.id.radioGrpVousEtes);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId == R.id.radioButnRecruteur) {
            return "Recruteur";
        } else if (selectedRadioButtonId == R.id.radioButnCandidat) {
            return "Candidat";
        } else {
            return "";
        }
    }

    private String getSelectedCity() {
        Spinner spinnerCity = findViewById(R.id.spinnerCity);
        return spinnerCity.getSelectedItem().toString();
    }

    private void insertUserData(String email, String password, String vousEtes, String ville) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_EMAIL, email);
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(UserContract.UserEntry.COLUMN_VOUS_ETES, vousEtes);
        values.put(UserContract.UserEntry.COLUMN_VILLE, ville);
        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
    }
}