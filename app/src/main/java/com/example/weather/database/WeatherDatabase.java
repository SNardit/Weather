package com.example.weather.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weather.dao.WeatherDao;
import com.example.weather.modelDataBase.City;

@Database(entities = {City.class}, version = 2, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();

}
