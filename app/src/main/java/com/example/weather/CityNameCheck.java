package com.example.weather;

import android.util.Log;

import com.example.weather.modelCityRequest.CityRequest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityNameCheck {

    private GoogleMap googleMap;
    private static final String TAG = "CityNameCheckActivity";

    public void initRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        googleMap = retrofit.create(GoogleMap.class);
    }

    public void requestRetrofitForCity (Float lat, Float lng, OnResponseCompletedCityRequest listener) {
        googleMap.loadCity(lat, lng).
                enqueue(new Callback<CityRequest>() {
                    @Override
                    public void onResponse(Call<CityRequest> call, Response<CityRequest> response) {

                        CityRequest cityRequest = response.body();
                        String cityName = cityRequest.getAddress().getCity();
                        listener.onCompleted(cityName);

                    }

                    @Override
                    public void onFailure(Call<CityRequest> call, Throwable t) {
                        Log.e(TAG, "error: " + t.getMessage());
                    }
                });
    }

}
