package com.example.weather.modelForLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest {
    @SerializedName("coord")
    @Expose
    private Coord coord;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

}
