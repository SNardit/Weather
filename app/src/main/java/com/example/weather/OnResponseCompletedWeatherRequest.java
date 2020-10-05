package com.example.weather;

import com.example.weather.modelForLocation.WeatherRequest;

public interface OnResponseCompletedWeatherRequest {
    void onCompleted(WeatherRequest weatherRequest, Float lat, Float lon);
}
