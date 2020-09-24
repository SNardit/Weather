package com.example.weather.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.AboutDevelopers;
import com.example.weather.CheckWeather;
import com.example.weather.R;
import com.example.weather.WeatherContainer;
import com.example.weather.modelOneCallWeather.OneCallRequest;
import com.example.weather.recyclerWeatherByDays.RecyclerDataAdapterWeatherByDays;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.RecyclerDataAdapterWeatherByHours;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WeatherFragment extends Fragment {
    private TextView cityTextView;
    private TextView dateView;
    private TextView tempTextView;
    private TextView descriptionTextView;


    private RecyclerView recyclerViewHours;
    private RecyclerDataAdapterWeatherByHours adapterWeatherByHours;
    private ArrayList<WeatherByHours> weatherByHours = new ArrayList<>();

    private RecyclerView recyclerViewDays;
    private RecyclerDataAdapterWeatherByDays adapterWeatherByDays;
    private ArrayList<WeatherByDays> weatherByDays = new ArrayList<>();

    private DialogBuilderFragment dlgBuilder;

    static WeatherFragment create(WeatherContainer container) {
        WeatherFragment fragment = new WeatherFragment();

        Bundle args = new Bundle();
        args.putParcelable("cityName", container);
        fragment.setArguments(args);
        return fragment;
    }

    String getCityName() {

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            WeatherContainer weatherContainer = bundle.getParcelable("cityName");
            assert weatherContainer != null;
            return weatherContainer.getCityName();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.weather_fragment, container, false);

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
        dlgBuilder = new DialogBuilderFragment();
        setCurrentWeather();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.menu_main, menu);
            menu.findItem(R.id.actSearch).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.infoAboutCity:
                setOnBtnInfoCity();
                return true;

            case R.id.infoAboutDevelopers:
                setOnBtnInfoDevelop();
                return true;
        }

        return false;
    }

    private void setCurrentWeather() {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread((Runnable) () -> {
            CheckWeather checkWeather = new CheckWeather();
            checkWeather.getCoordCity(getCityName());
            if (checkWeather.getWeatherRequest() == null) {
                dlgBuilder.show(getActivity().getSupportFragmentManager(), "dialogBuilder");
               /* Snackbar.make(cityTextView, "Choose entered city?", Snackbar.LENGTH_LONG).
                        //setText("Connection error or/and city is not exist!").show();*/
            }
            else {
                checkWeather.getCurrentWeather();
                handler.post(() -> displayWeather(checkWeather.getOneCallRequest()));
            }
        }).start();
    }

    public void displayWeather(OneCallRequest oneCallRequest) {
        int seconds = oneCallRequest.getTimezone_offset();

        String temperatureValue = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getCurrent().getTemp()) + "ºC";
        tempTextView.setText(temperatureValue);

        String descriptionText =  String.format(Locale.getDefault(), "%s", oneCallRequest.getCurrent().getWeather()[0].getDescription());
        descriptionTextView.setText(descriptionText);

        Date date = new Date((oneCallRequest.getCurrent().getDt() + seconds) * 1000);
        dateView.setText(makeDateFormat(date, "dd MMMM yyyy, EEEE"));

        for (int i = 0; i < 12; i++) {
            Date hourDate = new Date((oneCallRequest.getHourly()[i].getDt() + seconds) * 1000);
            String hour = makeDateFormat(hourDate, "HH");
            @SuppressLint("UseCompatLoadingForDrawables") Drawable pic = getResources().getDrawable(R.drawable.sunny);
            String temp = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getHourly()[i].getTemp()) + "ºC";
            weatherByHours.add(new WeatherByHours(hour, pic, temp));
        }
        setUpRecyclerViewHours();

        for (int i = 0; i < 3; i++) {
            Date dayDate = new Date((oneCallRequest.getDaily()[i].getDt() + seconds) * 1000);
            String day = makeDateFormat(dayDate, "EEEE");
            @SuppressLint("UseCompatLoadingForDrawables") Drawable pic = getResources().getDrawable(R.drawable.cloudy);
            String daytime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getDay()) + "ºC";
            String nighttime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getNight()) + "ºC";
            weatherByDays.add(new WeatherByDays(day, pic, daytime, nighttime));
        }
        setUpRecyclerViewDays();
    }

    private String makeDateFormat (Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private void initView(View view) {
        cityTextView = view.findViewById(R.id.textViewCity);
        dateView = view.findViewById(R.id.textViewDate);
        tempTextView = view.findViewById(R.id.textViewTemperature);
        descriptionTextView = view.findViewById(R.id.textViewDescription);
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
        String city = cityTextView.getText().toString();
        Uri uri = Uri.parse("https://wikipedia.org/wiki/" + city);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setOnBtnInfoDevelop() {
        Intent intent = new Intent();
        intent.setClass(requireActivity(), AboutDevelopers.class);
        startActivity(intent);
    }

}
