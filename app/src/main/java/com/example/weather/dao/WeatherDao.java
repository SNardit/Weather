package com.example.weather.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.modelDataBase.City;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(City city);

    @Update
    void updateCity(City city);

    @Delete
    void deleteCity(City city);

    @Query("UPDATE city SET date =  :date WHERE city_name = :city_name")
    void updateCityDateByCityName(String city_name, String date);

    @Query("UPDATE city SET weather =  :weather WHERE city_name = :city_name")
    void updateCityWeatherByCityName(String city_name, String weather);

    @Query("DELETE FROM city WHERE city_name = :city_name")
    void deleteCityByCityName(String city_name);

    @Query("SELECT * FROM city")
    List<City> getAllCities();

    @Query("SELECT * FROM city WHERE id = :id")
    City getCityById(long id);

    @Query("SELECT id FROM city WHERE city_name = :city_name")
    int getCityIdByCityName(String city_name);

    @Query("SELECT city_name FROM city WHERE id = :id")
    String getCityNameByCityId(long id);

    @Query("SELECT COUNT() FROM city")
    long getCountCity();

    @Query("DELETE FROM city WHERE id >= 1")
    void deleteCityFromId1();

    @Query("DELETE FROM city WHERE weather IS NULL")
    void deleteCityWithNullWeather();


}
