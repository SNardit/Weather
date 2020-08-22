package com.example.weather.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.recyclerWeatherByDays.RecyclerDataAdapterWeatherByDays;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.RecyclerDataAdapterWeatherByHours;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;
import com.example.weather.WeatherContainer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class WeatherFragment extends Fragment {
    private TextView cityTextView;
    private TextView dateView;
    private ImageView infoButton;

    private RecyclerView recyclerViewHours;
    private RecyclerDataAdapterWeatherByHours adapterWeatherByHours;
    private ArrayList<WeatherByHours> weatherByHours;

    private RecyclerView recyclerViewDays;
    private RecyclerDataAdapterWeatherByDays adapterWeatherByDays;
    private ArrayList<WeatherByDays> weatherByDays;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        fillUpArrayLists();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpRecyclerViewHours();
        setUpRecyclerViewDays();
        String cityName = getCityName();
        cityTextView.setText(cityName);
        setDate();
        setOnBtnInfoCity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fillUpArrayLists() {
        weatherByHours = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String hour = getResources().getStringArray(R.array.hours)[i];
            Drawable pic = getResources().getDrawable(R.drawable.sunny);
            String temp = getResources().getStringArray(R.array.temp)[i];
            weatherByHours.add(new WeatherByHours(hour, pic, temp));
        }

        weatherByDays = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String day = makeDayOfWeek(i);
            Drawable pic = getResources().getDrawable(R.drawable.cloudy);
            String daytime = getResources().getStringArray(R.array.temp)[i];
            String nighttime = getResources().getStringArray(R.array.temp)[i];
            weatherByDays.add(new WeatherByDays(day, pic, daytime, nighttime));
        }
    }

    private String makeDayOfWeek(int i) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_WEEK, i+1);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(cal.getTime());
    }

    private void initView(View view) {
        cityTextView = view.findViewById(R.id.textViewCity);
        dateView = view.findViewById(R.id.textViewDate);
        infoButton = view.findViewById(R.id.infoImageView);
        recyclerViewHours = view.findViewById(R.id.recyclerViewWeatherHour);
        recyclerViewDays = view.findViewById(R.id.recyclerViewWeatherDay);
    }

    private void setDate() {
        Date date = new Date();
        String dateFormat = String.format(getString(R.string.date_format), "", date);
        dateView.setText(dateFormat);
    }

    private void setUpRecyclerViewHours() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterWeatherByHours = new RecyclerDataAdapterWeatherByHours(weatherByHours);

        recyclerViewHours.setLayoutManager(layoutManager);
        recyclerViewHours.setAdapter(adapterWeatherByHours);
    }

    private void setUpRecyclerViewDays() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapterWeatherByDays = new RecyclerDataAdapterWeatherByDays(weatherByDays);

        recyclerViewDays.setLayoutManager(layoutManager);
        recyclerViewDays.setAdapter(adapterWeatherByDays);
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
