package com.example.weather;

import com.example.weather.modelForLocation.WeatherRequest;
import com.example.weather.modelOneCallWeather.OneCallRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadCoord(@Query("q") String cityCountry, @Query("units") String units, @Query("appid") String keyApi);

    @GET("data/2.5/onecall")
    Call<OneCallRequest> loadOneCallWeather(@Query("lat") Float lat, @Query("lon") Float lon, @Query("units") String units, @Query("appid") String keyApi);
}
