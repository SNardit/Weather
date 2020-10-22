package com.example.weather;

import com.example.weather.modelCityRequest.CityRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMap {
    @GET("reverse?format=json")
    Call<CityRequest> loadCity(@Query("lat") Float lat, @Query("lon") Float lng);
}
