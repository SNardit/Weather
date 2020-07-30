package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseCityActivity extends AppCompatActivity {
    private TextView  textViewMoscow;
    private TextView  textViewStPetersburg;
    private TextView  textViewOmsk;
    private TextView  textViewNewYork;
    private TextView  textViewLosAngeles;
    private TextView  textViewAnapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_choose_city);
        initViews();
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
    }

    private void clickOnCity(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
