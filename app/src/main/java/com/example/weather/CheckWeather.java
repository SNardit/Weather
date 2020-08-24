package com.example.weather;

import com.example.weather.modelForLocation.WeatherRequest;
import com.example.weather.modelOneCallWeather.OneCallRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CheckWeather {
    private static final String WEATHER_ONE_CALL_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String WEATHER_ONE_CALL_LATITUDE_URL = "lat=";
    private static final String WEATHER_ONE_CALL_LONGITUDE_URL = "&lon=";

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String WEATHER_URL_METRIC = "&units=metric&appid=";
    private static final String APP_ID = "2edc7c07fcaca5cef7f2aa2e3ba990bb";

    private WeatherRequest weatherRequest;
    private OneCallRequest oneCallRequest;

    public WeatherRequest getWeatherRequest() {
        return weatherRequest;
    }
    public OneCallRequest getOneCallRequest() {
        return oneCallRequest;
    }

    public void getCoordCity (String city) {
        try {
            final URL uri = new URL(WEATHER_URL + city + WEATHER_URL_METRIC + APP_ID);

            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = getLines(in);
                Gson gson = new Gson();
                weatherRequest = gson.fromJson(result, WeatherRequest.class);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentWeather(){

        try {
            final URL uri2 = new URL(WEATHER_ONE_CALL_URL + WEATHER_ONE_CALL_LATITUDE_URL + weatherRequest.getCoord().getLat()
                    + WEATHER_ONE_CALL_LONGITUDE_URL + weatherRequest.getCoord().getLon() + WEATHER_URL_METRIC + APP_ID);

            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri2.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = getLines(in);
                Gson gson = new Gson();
                oneCallRequest = gson.fromJson(result, OneCallRequest.class);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData.toString();
    }

}
