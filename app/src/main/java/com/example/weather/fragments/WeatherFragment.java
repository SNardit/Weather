package com.example.weather.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;
import com.example.weather.WeatherContainer;

import java.util.Date;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private TextView cityTextView;
    private TextView dateView;
    private ImageView infoButton;

    static WeatherFragment create(WeatherContainer container) {
        WeatherFragment fragment = new WeatherFragment();

        Bundle args = new Bundle();
        args.putSerializable("index", container);
        fragment.setArguments(args);
        return fragment;
    }

    int getIndex() {

        WeatherContainer weatherContainer = (WeatherContainer)
                (Objects.requireNonNull(getArguments()).getSerializable("index"));

        try {
            assert weatherContainer != null;
            return weatherContainer.position;
        } catch (Exception e) {
            return 0;
        }
    }

    String getCityName() {

        WeatherContainer weatherContainer = (WeatherContainer)
                (Objects.requireNonNull(getArguments()).getSerializable("index"));

        try {
            assert weatherContainer != null;
            return weatherContainer.cityName;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.weather_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        String cityName = getCityName();
        cityTextView.setText(cityName);
        setDate();
        setOnBtnInfoCity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        cityTextView = view.findViewById(R.id.textViewCity);
        dateView = view.findViewById(R.id.textViewDate);
        infoButton = view.findViewById(R.id.infoImageView);
    }

    private void setDate() {
        Date date = new Date();
        String dateFormat = String.format(getString(R.string.date_format), "", date);
        dateView.setText(dateFormat);
    }

    private void setOnBtnInfoCity() {
        infoButton.setOnClickListener(view -> {
            String city = cityTextView.getText().toString();
            Uri uri = Uri.parse("https://ru.wikipedia.org/wiki/" + city);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}
