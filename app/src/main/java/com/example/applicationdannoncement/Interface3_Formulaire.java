package com.example.applicationdannoncement;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Interface3_Formulaire extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextTitre;
    private EditText editTextTypeContrat;
    private EditText editTextDescription;
    private Spinner spinnerCategorie, spinnerSecteur, spinnerVille;
    private Button buttonSuivant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface3_formulaire);

        editTextTitre = (EditText) findViewById(R.id.EditTextText);
        editTextTypeContrat = findViewById(R.id.contractSaisie);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategorie = findViewById(R.id.choixCateg);
        spinnerSecteur = findViewById(R.id.choixSecteur);
        spinnerVille = findViewById(R.id.choixville);
        buttonSuivant = findViewById(R.id.SuivantBtn);

        databaseHelper = new DatabaseHelper(this);

        setupSpinners();

        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDatabase();
            }
        });
    }

    private void setupSpinners() {
        List<String> categories = new ArrayList<>();
        categories.add("Devloppement web");
        categories.add("Devloppement mobile");
        categories.add("Marketing digitale");
        categories.add("Automobile");
        categories.add("Internet des objects");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(categoryAdapter);

        List<String> sectors = new ArrayList<>();
        sectors.add("Informatique");
        sectors.add("Industriel");
        sectors.add("Mecanique");
        sectors.add("Electrique");
        sectors.add("Marketing");
        ArrayAdapter<String> sectorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectors);
        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecteur.setAdapter(sectorAdapter);

        List<String> cities = new ArrayList<>();
        cities.add("Rabat");
        cities.add("Marrakech");
        cities.add("Oujda");
        cities.add("Errachidia");
        cities.add("El Jadida");
        cities.add("Casablanca");
        cities.add("Agadir");
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(cityAdapter);
    }

    private void sendDataToDatabase() {
        String titre = editTextTitre.getText().toString().trim();
        String typeContrat = editTextTypeContrat.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String categorie = spinnerCategorie.getSelectedItem().toString();
        String secteur = spinnerSecteur.getSelectedItem().toString();
        String ville = spinnerVille.getSelectedItem().toString();

        if (titre.isEmpty() || typeContrat.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = databaseHelper.insertData(titre, typeContrat, description, categorie, secteur, ville);

        if (isSuccess) {
            Intent intent = new Intent(Interface3_Formulaire.this, VillePage.class);
            intent.putExtra("selectedCity", ville);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to send data to database", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Utilisateur.db";
        private static final String TABLE_NAME = "Annonce_Formulaire";
        private static final String COL_ID = "id";
        private static final String COL_TITRE = "Titre";
        private static final String COL_TYPE_CONTRAT = "Type_Contrat";
        private static final String COL_DESCRIPTION = "Description";
        private static final String COL_CATEGORIE = "Categorie";
        private static final String COL_SECTEUR = "Secteur";
        private static final String COL_VILLE = "Ville";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITRE + " TEXT, " +
                    COL_TYPE_CONTRAT + " TEXT, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_CATEGORIE + " TEXT, " +
                    COL_SECTEUR + " TEXT, " +
                    COL_VILLE + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertData(String Titre, String Type_Contrat, String Description,
                                  String Categorie, String Secteur, String Ville) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_TITRE, Titre);
            contentValues.put(COL_TYPE_CONTRAT, Type_Contrat);
            contentValues.put(COL_DESCRIPTION, Description);
            contentValues.put(COL_CATEGORIE, Categorie);
            contentValues.put(COL_SECTEUR, Secteur);
            contentValues.put(COL_VILLE, Ville);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }
}
