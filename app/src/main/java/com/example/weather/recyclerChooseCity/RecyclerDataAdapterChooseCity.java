package com.example.weather.recyclerChooseCity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.WeatherSource;
import com.example.weather.modelDataBase.City;

import java.util.List;

public class RecyclerDataAdapterChooseCity extends RecyclerView.Adapter<RecyclerDataAdapterChooseCity.ViewHolder> {

    private WeatherSource dataSource;
    private IRVOnItemClick onItemClickChooseCity;

    public RecyclerDataAdapterChooseCity(WeatherSource dataSource, IRVOnItemClick onItemClickChooseCity){
        this.dataSource = dataSource;
        this.onItemClickChooseCity = onItemClickChooseCity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<City> cities = dataSource.getCities();

        City city = cities.get(position);

        holder.cityNameView.setText(city.cityName);
        holder.cityDateView.setText(city.date);
        holder.cityWeatherView.setText(city.weather);
        holder.setOnClickForItem(city.cityName);
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountCities();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameView;
        TextView cityDateView;
        TextView cityWeatherView;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            cityNameView = itemView.findViewById(R.id.cityTextView);
            cityDateView = itemView.findViewById(R.id.dateTextView);
            cityWeatherView = itemView.findViewById(R.id.weatherTextView);
        }

        void setOnClickForItem(final String city) {
            itemView.setOnClickListener(view -> {
                if(onItemClickChooseCity != null) {
                    onItemClickChooseCity.onItemClick(city);
                }
            });
        }

    }
}
