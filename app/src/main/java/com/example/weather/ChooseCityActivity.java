package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChooseCityActivity extends AppCompatActivity {
    private TextView  textViewMoscow;
    private TextView  textViewStPetersburg;
    private TextView  textViewOmsk;
    private TextView  textViewNewYork;
    private TextView  textViewLosAngeles;
    private TextView  textViewAnapa;
    private EditText searchCity;

    final static String dataKey = "dataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_choose_city);
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clickOnCity(textViewMoscow);
        clickOnCity(textViewStPetersburg);
        clickOnCity(textViewOmsk);
        clickOnCity(textViewNewYork);
        clickOnCity(textViewLosAngeles);
        clickOnCity(textViewAnapa);
    }

    private void initViews() {
        textViewMoscow = findViewById(R.id.textViewMoscow);
        textViewStPetersburg = findViewById(R.id.textViewStPetersburg);
        textViewOmsk = findViewById(R.id.textViewOmsk);
        textViewNewYork = findViewById(R.id.textViewNewYork);
        textViewLosAngeles = findViewById(R.id.textViewLosAngeles);
        textViewAnapa = findViewById(R.id.textViewAnapa);
        searchCity = findViewById(R.id.searchCity);
    }

    private void clickOnCity(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCity.setText(textView.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String text = searchCity.getText().toString();

            Intent dataIntent = new Intent();
            dataIntent.putExtra(dataKey, text);
            setResult(RESULT_OK, dataIntent);
            finish();
        }
        return true;
    }
}
