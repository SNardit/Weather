package com.example.weather.recyclerWeatherByHours;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;

import java.util.ArrayList;

public class RecyclerDataAdapterWeatherByHours extends RecyclerView.Adapter<RecyclerDataAdapterWeatherByHours.ViewHolder> {
    private ArrayList<WeatherByHours> weather;

    public RecyclerDataAdapterWeatherByHours(ArrayList<WeatherByHours> weather) {
        if (weather != null) {
            this.weather = weather;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewHour.setText(weather.get(position).hour);
        holder.weatherPic.setImageResource(weather.get(position).image);
        holder.textViewWeather.setText(weather.get(position).weather);
    }

    @Override
    public int getItemCount() {
        return weather == null ? 0 : weather.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHour;
        ImageView weatherPic;
        TextView textViewWeather;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewHour = itemView.findViewById(R.id.textViewHour);
            weatherPic = itemView.findViewById(R.id.imageViewHourWeatherPic);
            textViewWeather = itemView.findViewById(R.id.textViewHourTemp);
        }
    }
}
