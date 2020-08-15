package com.example.weather.recyclerWeatherByDays;

import android.graphics.drawable.Drawable;

public class WeatherByDays {
    String day;
    Drawable image;
    String weatherDaytime;
    String weatherNighttime;

    public WeatherByDays(String day, Drawable image, String weatherDaytime, String weatherNighttime) {
        this.day = day;
        this.image = image;
        this.weatherDaytime = weatherDaytime;
        this.weatherNighttime = weatherNighttime;
    }
}
