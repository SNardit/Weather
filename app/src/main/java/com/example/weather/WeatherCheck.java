package com.example.weather;

import android.util.Log;

import com.example.weather.modelForLocation.WeatherRequest;
import com.example.weather.modelOneCallWeather.OneCallRequest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherCheck {

    private OpenWeather openWeather;
    private static final String APP_ID = "2edc7c07fcaca5cef7f2aa2e3ba990bb";
    private static final String TAG = "CheckWeatherActivity";

    private Float lat;
    private Float lon;

    public void initRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        openWeather = retrofit.create(OpenWeather.class);
        
    }

    public void requestRetrofitForCoord (String city, OnResponseCompletedWeatherRequest listener) {
        openWeather.loadCoord(city, "metric", APP_ID).
                enqueue(new Callback<WeatherRequest>() {
                @Override
                public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {

                    WeatherRequest weatherRequest = response.body();
                    if (response.body() != null) {
                        lat = weatherRequest.getCoord().getLat();
                        lon = weatherRequest.getCoord().getLon();
                    }
                    listener.onCompleted(weatherRequest, lat, lon);

                }

                @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    Log.e(TAG, "error: " + t.getMessage());
                }
            });
    }

    public void requestRetrofitForWeather (Float lat, Float lon, OnResponseCompletedOneCall listener) {
        openWeather.loadOneCallWeather(lat, lon, "metric", APP_ID)
                .enqueue(new Callback<OneCallRequest>() {
                    @Override
                    public void onResponse(Call<OneCallRequest> call, Response<OneCallRequest> response) {
                        OneCallRequest oneCallRequest = response.body();
                        listener.onCompletedCall(oneCallRequest);
                    }

                    @Override
                    public void onFailure(Call<OneCallRequest> call, Throwable t) {
                        Log.e(TAG, "error: " + t.getMessage());
                    }
                });
    }

}
