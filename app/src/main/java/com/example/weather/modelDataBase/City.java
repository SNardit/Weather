package com.example.weather.modelDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index(value = {"city_name"}, unique = true)})
public class City {

    public final static String ID = "id";
    public final static String CITY_NAME = "city_name";
    public final static String DATE = "date";
    public final static String WEATHER = "weather";

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
