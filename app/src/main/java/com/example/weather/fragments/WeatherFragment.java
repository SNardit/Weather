package com.example.weather.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.AboutDevelopers;
import com.example.weather.R;
import com.example.weather.WeatherContainer;
import com.example.weather.WeatherService;
import com.example.weather.fragments.flavor.ChooseCityFragment;
import com.example.weather.recyclerWeatherByDays.RecyclerDataAdapterWeatherByDays;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.RecyclerDataAdapterWeatherByHours;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


public class WeatherFragment extends Fragment {

    private String cityName;

    private TextView cityTextView;
    private TextView dateView;
    private TextView tempTextView;
    private TextView descriptionTextView;
    private ImageView backgroundView;
    private ArrayList<String> picWeatherArray;
    private Integer arrayPosition = null;

    private static final String TAG = "WeatherFragment";
    private int messageId = 100;

    private RecyclerView recyclerViewHours;
    private ArrayList<WeatherByHours> weatherByHours = new ArrayList<>();

    private RecyclerView recyclerViewDays;
    private ArrayList<WeatherByDays> weatherByDays = new ArrayList<>();

    private DialogBuilderFragment dlgBuilder;

    private static final String CURRENT_POSITION = "Current position";
    private static final String CURRENT_ID = "currentId";

    private static Float latitude;
    private static Float longitude;

    public static final String BROADCAST_ACTION = "com.example.weather";
    public static final String TEMP = "temp";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String DATE_HISTORY = "dateHistory";
    public static final String WEATHER_BY_HOURS = "weatherByHours";
    public static final String WEATHER_BY_DAYS = "weatherByDays";
    public static final String NO_CITY = "noCity";
    public static final String CITY_NAME = "cityName";
    public static final String DATABASE_CITY = "dataBaseCity";
    public static final String WARNING_TEMPERATURE = "warningTemperature";


    public static WeatherFragment create(WeatherContainer container) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable("cityName", container);
        fragment.setArguments(args);
        return fragment;
    }

    public static WeatherFragment create(Float lat, Float lng) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putFloat("latitude", lat);
        args.putFloat("longitude", lng);
        fragment.setArguments(args);
        return fragment;
    }

    public String getCityName() {

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            WeatherContainer weatherContainer = bundle.getParcelable(getString(R.string.city_name_string));
            assert weatherContainer != null;
            return weatherContainer.getCityName();
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
            return "";
        }
    }

    public Float getLatitude() {

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            return bundle.getFloat(getString(R.string.latitude));
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
            return null;
        }
    }

    public Float getLongitude() {

        Bundle bundle = this.getArguments();
        try {
            assert bundle != null;
            return bundle.getFloat(getString(R.string.longitude));
        } catch (Exception e) {
            Log.e(TAG, "error: ", e);
            return null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.weather_fragment, container, false);

        IntentFilter intFilter = new IntentFilter(BROADCAST_ACTION);
        requireActivity().registerReceiver(br, intFilter);
        initNotificationChannel();

        return view;
    }

    private final BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra(TEMP);
            Integer id = intent.getIntExtra(ID, 0);
            arrayPosition = getPicById(id);

            if (arrayPosition != null) {
                Picasso.get()
                        .load(picWeatherArray.get(arrayPosition))
                        .into(backgroundView);
            }

            String dataBaseCity = intent.getStringExtra(DATABASE_CITY);
            String description = intent.getStringExtra(DESCRIPTION);
            String date = intent.getStringExtra(DATE);
            String dateHistory = intent.getStringExtra(DATE_HISTORY);
            String geoCityName = intent.getStringExtra(CITY_NAME);
            String warningTemperature = intent.getStringExtra(WARNING_TEMPERATURE);

            if (intent.getIntExtra(NO_CITY, -1) == 0) {
                dlgBuilder.show(requireActivity().getSupportFragmentManager(), getString(R.string.dialog_builder));
            } else {

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CURRENT_POSITION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(CURRENT_ID, ChooseCityFragment.getCurrentPosition());
                editor.apply();

                if (geoCityName != null) {
                    cityTextView.setText(geoCityName);
                }
                if (warningTemperature != null) {
                    notification(warningTemperature);
                }
                tempTextView.setText(temp);
                descriptionTextView.setText(description);
                dateView.setText(date);
                ChooseCityFragment.weatherSource.updateCityDateByCityName(dataBaseCity, dateHistory);
                ChooseCityFragment.weatherSource.updateCityWeatherByCityName(dataBaseCity, temp);

                weatherByHours.addAll(Objects.requireNonNull(intent.getParcelableArrayListExtra(WEATHER_BY_HOURS)));
                setUpRecyclerViewHours();

                weatherByDays.addAll(Objects.requireNonNull(intent.getParcelableArrayListExtra(WEATHER_BY_DAYS)));
                setUpRecyclerViewDays();
            }

        }
    };

    private void initGetToken(View view) {

        final EditText textToken = view.findViewById(R.id.textToken);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(getString(R.string.push_message), getString(R.string.get_instance_id_filed), task.getException());
                        return;
                    }

                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    textToken.setText(token);
                });
    }

    private void notification (String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.weather_notication))
                .setContentText(message);
        NotificationManager notificationManager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initGetToken(view);
        setUpRecyclerViewHours();
        setUpRecyclerViewDays();
        cityName = getCityName();
        latitude = getLatitude();
        longitude = getLongitude();
        cityTextView.setText(cityName);
        setDate();
        dlgBuilder = new DialogBuilderFragment();
        getWeatherData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            requireActivity().unregisterReceiver(br);
        }catch (Exception e){
            Log.d(TAG, getString(R.string.failed), e);
        }
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
        if (latitude != 0.0 && longitude != 0.0) {
            intent.putExtra(getString(R.string.latitude), latitude);
            intent.putExtra(getString(R.string.longitude), longitude);
            requireActivity().startService(intent);
        } else {
            requireActivity().startService(intent.putExtra(getString(R.string.city), getCityName()));
        }
    }

    private void initView(View view) {
        cityTextView = view.findViewById(R.id.textViewCity);
        dateView = view.findViewById(R.id.textViewDate);
        tempTextView = view.findViewById(R.id.textViewTemperature);
        descriptionTextView = view.findViewById(R.id.textViewDescription);
        backgroundView = view.findViewById(R.id.background);
        recyclerViewHours = view.findViewById(R.id.recyclerViewWeatherHour);
        recyclerViewDays = view.findViewById(R.id.recyclerViewWeatherDay);
        picWeatherArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pic_for_background)));
    }

    private void setDate() {
        Date date = new Date();
        String dateFormat = String.format(getString(R.string.date_format), "", date);
        dateView.setText(dateFormat);
    }

    private void setUpRecyclerViewHours() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerDataAdapterWeatherByHours adapterWeatherByHours = new RecyclerDataAdapterWeatherByHours(weatherByHours);

        recyclerViewHours.setLayoutManager(layoutManager);
        recyclerViewHours.setAdapter(adapterWeatherByHours);
    }

    private void setUpRecyclerViewDays() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerDataAdapterWeatherByDays adapterWeatherByDays = new RecyclerDataAdapterWeatherByDays(weatherByDays);

        recyclerViewDays.setLayoutManager(layoutManager);
        recyclerViewDays.setAdapter(adapterWeatherByDays);
    }

    private void setOnBtnInfoCity() {
        String city = cityTextView.getText().toString();
        Uri uri = Uri.parse(getString(R.string.url_wiki) + city);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setOnBtnInfoDevelop() {
        Intent intent = new Intent();
        intent.setClass(requireActivity(), AboutDevelopers.class);
        startActivity(intent);
    }

    private Integer getPicById (Integer id) {
        if (id >= 200 && id < 300) { arrayPosition = 0; }
        else if (id >= 300 && id < 400) { arrayPosition = 1; }
        else if (id >= 500 && id < 600) { arrayPosition = 2; }
        else if (id >= 600 && id < 700) { arrayPosition = 3; }
        else if (id >= 700 && id < 800) { arrayPosition = 4; }
        else if (id == 800) { arrayPosition = 5; }
        else if (id > 800 && id <= 804) { arrayPosition = 6; }
        return arrayPosition;
    }

    @Override
    public void onDetach() {
        ChooseCityFragment.weatherSource.removeCityWithNullWeather();
        super.onDetach();
    }

}
