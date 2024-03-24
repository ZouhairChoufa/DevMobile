package com.example.applicationdannoncement;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;


public class VillePage extends AppCompatActivity {

    private TextView textViewVille;
    private TextView textViewNumber;

    private Interface3_Formulaire.DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ville_page);


        textViewVille = findViewById(R.id.textViewVille);
        textViewNumber = findViewById(R.id.textViewNumber);

        databaseHelper = new Interface3_Formulaire.DatabaseHelper(this);

        String selectedCity = getIntent().getStringExtra("Selected City");

        int cityNumber = getCityNumber(selectedCity);

        textViewVille.setText(selectedCity);
        textViewNumber.setText(String.valueOf(cityNumber));
    }
    private int getCityNumber(String city) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM formulaire WHERE ville = ?", new String[]{city});
        int cityCount = 0;
        if (cursor.moveToFirst()) {
            cityCount = cursor.getInt(0);
        }
        cursor.close();
        return cityCount;
    }
}