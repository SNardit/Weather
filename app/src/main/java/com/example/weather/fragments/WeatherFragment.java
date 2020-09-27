package com.example.weather.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.AboutDevelopers;
import com.example.weather.R;
import com.example.weather.WeatherContainer;
import com.example.weather.WeatherService;
import com.example.weather.recyclerWeatherByDays.RecyclerDataAdapterWeatherByDays;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.RecyclerDataAdapterWeatherByHours;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;

import java.util.ArrayList;
import java.util.Date;


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

    public final static String BROADCAST_ACTION = "com.example.weather";
    public final static String TEMP = "temp";
    public final static String DESCRIPTION = "description";
    public final static String DATE = "date";
    public final static String WEATHER_BY_HOURS = "weatherByHours";
    public final static String WEATHER_BY_DAYS = "weatherByDays";
    public final static String NO_CITY = "noCity";


    static WeatherFragment create(WeatherContainer container) {
        WeatherFragment fragment = new WeatherFragment();

        Bundle args = new Bundle();
        args.putParcelable("cityName", container);
        fragment.setArguments(args);
        return fragment;
    }

    public String getCityName() {

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

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getActivity().registerReceiver(br, intFilt);
        initNotificationChannel();

        return view;
    }

    private final BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra(TEMP);
            String description = intent.getStringExtra(DESCRIPTION);
            String date = intent.getStringExtra(DATE);

            if (intent.getIntExtra(NO_CITY, -1) == 0) {
                dlgBuilder.show(getActivity().getSupportFragmentManager(), "dialogBuilder");
            } else {
                tempTextView.setText(temp);
                descriptionTextView.setText(description);
                dateView.setText(date);

                weatherByHours.addAll(intent.getParcelableArrayListExtra(WEATHER_BY_HOURS));
                setUpRecyclerViewHours();

                weatherByDays.addAll(intent.getParcelableArrayListExtra(WEATHER_BY_DAYS));
                setUpRecyclerViewDays();
            }

        }
    };

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
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
        getWeatherData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(br);
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

    private void getWeatherData() {
        Intent intent = new Intent(getActivity(), WeatherService.class);
        getActivity().startService(intent.putExtra("city", getCityName()));

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
