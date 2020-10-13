package com.example.weather;

import com.example.weather.dao.WeatherDao;
import com.example.weather.modelDataBase.City;

import java.util.List;

public class WeatherSource {

    private final WeatherDao weatherDao;

    private List<City> cities;

    public WeatherSource(WeatherDao weatherDao){
        this.weatherDao = weatherDao;
    }

    public List<City> getCities(){

        if (cities == null){
            LoadCities();
        }
        return cities;
    }

    public void LoadCities(){
        cities = weatherDao.getAllCities();
    }


    public long getCountCities(){
        return weatherDao.getCountCity();
    }

    public int getIdByCityName(String cityName){
        return weatherDao.getCityIdByCityName(cityName);
    }

    public String getCityNameById(long id){
        return weatherDao.getCityNameByCityId(id);
    }

    public void updateCityDateByCityName(String cityName, String date) {
        weatherDao.updateCityDateByCityName(cityName, date);
        LoadCities();
    }

    public void updateCityWeatherByCityName(String cityName, String weather) {
        weatherDao.updateCityWeatherByCityName(cityName, weather);
        LoadCities();
    }

    public void addCity(City city){
        weatherDao.insertCity(city);
        LoadCities();
    }

    public void updateCity(City city){
        weatherDao.updateCity(city);
        LoadCities();
    }

    public void removeCity(String cityName){
        weatherDao.deleteCityByCityName(cityName);
        LoadCities();
    }

    public void removeAllCity(){
        weatherDao.deleteCityFromId1();
        LoadCities();
    }

    public void removeCityWithNullWeather(){
        weatherDao.deleteCityWithNullWeather();
        LoadCities();
    }

}

