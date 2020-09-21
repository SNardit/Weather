package com.example.weather;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherContainer implements Parcelable {

    private String cityName;

    public WeatherContainer(String cityName) {
        this.cityName = cityName;
    }

    public WeatherContainer(Parcel in) {
        cityName = in.readString();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityName);
    }

    public static final Creator<WeatherContainer> CREATOR = new Creator<WeatherContainer>() {
        @Override
        public WeatherContainer createFromParcel(Parcel in) {
            return new WeatherContainer(in);
        }

        @Override
        public WeatherContainer[] newArray(int size) {
            return new WeatherContainer[size];
        }
    };
}



