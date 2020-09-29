package com.example.weather.recyclerWeatherByHours;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherByHours implements Parcelable {
    String hour;
    Integer image;
    String weather;

    public WeatherByHours(String hour, Integer image, String weather) {
        this.hour = hour;
        this.image = image;
        this.weather = weather;
    }

    protected WeatherByHours(Parcel in) {
        hour = in.readString();
        image = in.readInt();
        weather = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hour);
        dest.writeInt(image);
        dest.writeString(weather);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherByHours> CREATOR = new Creator<WeatherByHours>() {
        @Override
        public WeatherByHours createFromParcel(Parcel in) {
            return new WeatherByHours(in);
        }

        @Override
        public WeatherByHours[] newArray(int size) {
            return new WeatherByHours[size];
        }
    };
}
