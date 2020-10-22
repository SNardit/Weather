package com.example.weather.recyclerWeatherByDays;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherByDays implements Parcelable {
    String day;
    Integer image;
    String weatherDaytime;
    String weatherNighttime;

    public WeatherByDays(String day, Integer image, String weatherDaytime, String weatherNighttime) {
        this.day = day;
        this.image = image;
        this.weatherDaytime = weatherDaytime;
        this.weatherNighttime = weatherNighttime;
    }

    protected WeatherByDays(Parcel in) {
        day = in.readString();
        image = in.readInt();
        weatherDaytime = in.readString();
        weatherNighttime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeInt(image);
        dest.writeString(weatherDaytime);
        dest.writeString(weatherNighttime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherByDays> CREATOR = new Creator<WeatherByDays>() {
        @Override
        public WeatherByDays createFromParcel(Parcel in) {
            return new WeatherByDays(in);
        }

        @Override
        public WeatherByDays[] newArray(int size) {
            return new WeatherByDays[size];
        }
    };
}
