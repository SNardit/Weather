package com.example.weather;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.weather.fragments.WeatherFragment;
import com.example.weather.modelOneCallWeather.OneCallRequest;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WeatherService extends IntentService {

    private String city;
    private String dataBaseCity;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        WeatherCheck weatherCheck = new WeatherCheck();
        weatherCheck.initRetrofit();
        if (intent.getStringExtra(getString(R.string.city)) != null) {
            dataBaseCity = intent.getStringExtra(getString(R.string.city));
            weatherCheck.requestRetrofitForCoord(intent.getStringExtra(getString(R.string.city)), (weatherRequest, lat, lon) -> {
                if (weatherRequest == null) {
                    Intent noCityIntent = new Intent(WeatherFragment.BROADCAST_ACTION);
                    noCityIntent.putExtra(WeatherFragment.NO_CITY, 0);
                    sendBroadcast(noCityIntent);
                } else {
                    weatherCheck.requestRetrofitForWeather(lat, lon, this::displayWeather);
                }
            });
        } else {
            CityNameCheck cityNameCheck = new CityNameCheck();
            cityNameCheck.initRetrofit();
            Float lat = intent.getFloatExtra(getString(R.string.latitude), 0);
            Float lon = intent.getFloatExtra(getString(R.string.longitude), 0);
            cityNameCheck.requestRetrofitForCity(lat, lon, cityName -> {
                city = cityName;
                weatherCheck.requestRetrofitForWeather(lat, lon, this::displayWeather);
            });

        }
    }

    public void displayWeather(OneCallRequest oneCallRequest) {
        int seconds = oneCallRequest.getTimezoneOffset();

        Intent intent = new Intent(WeatherFragment.BROADCAST_ACTION);
        intent.putExtra(WeatherFragment.CITY_NAME, city);
        intent.putExtra(WeatherFragment.DATABASE_CITY, dataBaseCity);

        String temperature = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getCurrent().getTemp()) + getString(R.string.celsius);
        intent.putExtra(WeatherFragment.TEMP, temperature);

        float temps = oneCallRequest.getCurrent().getTemp();
        if (temps < 0) {
            intent.putExtra(WeatherFragment.WARNING_TEMPERATURE, getString(R.string.show_warning_be_careful));
        }

        Integer id = oneCallRequest.getCurrent().getWeather()[0].getId();
        intent.putExtra(WeatherFragment.ID, id);

        String description = String.format(Locale.getDefault(), "%s", oneCallRequest.getCurrent().getWeather()[0].getMain());
        intent.putExtra(WeatherFragment.DESCRIPTION, description);

        Date date = new Date((oneCallRequest.getCurrent().getDt() + seconds) * 1000);
        String dateFormat = makeDateFormat(date, getString(R.string.date_format_for_today));
        intent.putExtra(WeatherFragment.DATE, dateFormat);

        String dateFormatHistory = makeDateFormat(date, getString(R.string.date_format_for_history));
        intent.putExtra(WeatherFragment.DATE_HISTORY, dateFormatHistory);


        ArrayList<WeatherByHours> weatherByHours = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Date hourDate = new Date((oneCallRequest.getHourly()[i].getDt() + seconds) * 1000);
            String hour = makeDateFormat(hourDate, getString(R.string.date_format_for_hourly));
            String icon = "p" + String.format(Locale.getDefault(), "%s", oneCallRequest.getHourly()[i].getWeather()[0].getIcon());
            Integer pic = getBaseContext().getResources().getIdentifier(icon, "drawable", getPackageName());
            String temp = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getHourly()[i].getTemp()) + getString(R.string.celsius);
            weatherByHours.add(new WeatherByHours(hour, pic, temp));
        }
        intent.putParcelableArrayListExtra(WeatherFragment.WEATHER_BY_HOURS, weatherByHours);

        ArrayList<WeatherByDays> weatherByDays = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Date dayDate = new Date((oneCallRequest.getDaily()[i].getDt() + seconds) * 1000);
            String day = makeDateFormat(dayDate, getString(R.string.date_format_for_daytime));
            String icon = "p" + String.format(Locale.getDefault(), "%s", oneCallRequest.getDaily()[i].getWeather()[0].getIcon());
            Integer pic = getBaseContext().getResources().getIdentifier(icon, "drawable", getPackageName());
            String daytime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getDay()) + getString(R.string.celsius);
            String nighttime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getNight()) + getString(R.string.celsius);
            weatherByDays.add(new WeatherByDays(day, pic, daytime, nighttime));
        }
        intent.putParcelableArrayListExtra(WeatherFragment.WEATHER_BY_DAYS, weatherByDays);

        sendBroadcast(intent);
    }

    private String makeDateFormat (Date date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
