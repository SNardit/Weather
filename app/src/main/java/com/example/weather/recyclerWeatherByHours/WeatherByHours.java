package com.example.weather.recyclerWeatherByHours;

import android.graphics.drawable.Drawable;

public class WeatherByHours {
    String hour;
    Drawable image;
    String weather;

    public WeatherByHours(String hour, Drawable image, String weather) {
        this.hour = hour;
        this.image = image;
        this.weather = weather;
    }
}
