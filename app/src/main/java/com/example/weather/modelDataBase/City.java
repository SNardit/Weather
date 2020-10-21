package com.example.weather.modelDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index(value = {"city_name"}, unique = true)})
public class City {

    public static final String ID = "id";
    public static final String CITY_NAME = "city_name";
    public static final String DATE = "date";
    public static final String WEATHER = "weather";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;

    @ColumnInfo(name = CITY_NAME)
    public String cityName;

    @ColumnInfo(name = DATE)
    public String date;

    @ColumnInfo(name = WEATHER)
    public String weather;

}
