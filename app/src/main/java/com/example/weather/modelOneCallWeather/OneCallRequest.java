package com.example.weather.modelOneCallWeather;

public class OneCallRequest {
    private String timezone;
    private int timezone_offset;
    private Current current;
    private Hourly[] hourly;
    private Daily[] daily;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(int timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hourly[] getHourly() {
        return hourly;
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public void setDaily(Daily[] daily) {
        this.daily = daily;
    }
}
