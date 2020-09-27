package com.example.weather.recyclerWeatherByDays;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;

import java.util.ArrayList;

public class RecyclerDataAdapterWeatherByDays extends RecyclerView.Adapter<RecyclerDataAdapterWeatherByDays.ViewHolder> {
    private ArrayList<WeatherByDays> weatherDays;

    public RecyclerDataAdapterWeatherByDays(ArrayList<WeatherByDays> weatherDays) {
       if (weatherDays != null) {
            this.weatherDays = weatherDays;
       }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewDay.setText(weatherDays.get(position).day);
        holder.weatherPicDay.setImageResource(weatherDays.get(position).image);
        holder.textViewDaytimeWeather.setText(weatherDays.get(position).weatherDaytime);
        holder.textViewNighttimeWeather.setText(weatherDays.get(position).weatherNighttime);
    }

    @Override
    public int getItemCount() {
        return weatherDays == null ? 0 : weatherDays.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;
        ImageView weatherPicDay;
        TextView textViewDaytimeWeather;
        TextView textViewNighttimeWeather;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.textViewDay);
            weatherPicDay = itemView.findViewById(R.id.imageViewDayWeatherPic);
            textViewDaytimeWeather = itemView.findViewById(R.id.textViewDaytimeTemp);
            textViewNighttimeWeather = itemView.findViewById(R.id.textViewNighttimeTemp);
        }
    }
}
