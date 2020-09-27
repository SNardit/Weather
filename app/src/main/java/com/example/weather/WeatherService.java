package com.example.weather;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.weather.fragments.WeatherFragment;
import com.example.weather.modelOneCallWeather.OneCallRequest;
import com.example.weather.recyclerWeatherByDays.WeatherByDays;
import com.example.weather.recyclerWeatherByHours.WeatherByHours;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WeatherService extends IntentService {

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Handler handler = new Handler(Looper.getMainLooper());
        CheckWeather checkWeather = new CheckWeather();
        checkWeather.getCoordCity(intent.getStringExtra("city"));
            if (checkWeather.getWeatherRequest() == null) {
                Intent noCityIntent = new Intent(WeatherFragment.BROADCAST_ACTION);
                noCityIntent.putExtra(WeatherFragment.NO_CITY, 0);
                sendBroadcast(noCityIntent);
            }
            else {
                checkWeather.getCurrentWeather();
                handler.post(() -> displayWeather(checkWeather.getOneCallRequest()));
            }
    }

    public void displayWeather(OneCallRequest oneCallRequest) {
        int seconds = oneCallRequest.getTimezone_offset();

        Intent intent = new Intent(WeatherFragment.BROADCAST_ACTION);

        String temp1 = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getCurrent().getTemp()) + "ºC";
        intent.putExtra(WeatherFragment.TEMP, temp1);

        String description = String.format(Locale.getDefault(), "%s", oneCallRequest.getCurrent().getWeather()[0].getDescription());
        intent.putExtra(WeatherFragment.DESCRIPTION, description);

        Date date = new Date((oneCallRequest.getCurrent().getDt() + seconds) * 1000);
        String dateFormat = makeDateFormat(date, "dd MMMM yyyy, EEEE");
        intent.putExtra(WeatherFragment.DATE, dateFormat);


        ArrayList<WeatherByHours> weatherByHours = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Date hourDate = new Date((oneCallRequest.getHourly()[i].getDt() + seconds) * 1000);
            String hour = makeDateFormat(hourDate, "HH");
            Integer pic = R.drawable.sunny;
            String temp = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getHourly()[i].getTemp()) + "ºC";
            weatherByHours.add(new WeatherByHours(hour, pic, temp));
        }
        intent.putParcelableArrayListExtra(WeatherFragment.WEATHER_BY_HOURS, weatherByHours);

        ArrayList<WeatherByDays> weatherByDays = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Date dayDate = new Date((oneCallRequest.getDaily()[i].getDt() + seconds) * 1000);
            String day = makeDateFormat(dayDate, "EEEE");
            Integer pic = R.drawable.cloudy;
            String daytime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getDay()) + "ºC";
            String nighttime = String.format(Locale.getDefault(), "%.0f", oneCallRequest.getDaily()[i].getTemp().getNight()) + "ºC";
            weatherByDays.add(new WeatherByDays(day, pic, daytime, nighttime));
        }
        intent.putParcelableArrayListExtra(WeatherFragment.WEATHER_BY_DAYS, weatherByDays);

        sendBroadcast(intent);
    }

    private String makeDateFormat (Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
