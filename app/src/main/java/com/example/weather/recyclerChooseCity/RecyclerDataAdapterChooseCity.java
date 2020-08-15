package com.example.weather.recyclerChooseCity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;

import java.util.ArrayList;

public class RecyclerDataAdapterChooseCity extends RecyclerView.Adapter<RecyclerDataAdapterChooseCity.ViewHolder> {
    private ArrayList<String> cities;
    private IRVOnItemClick onItemClickChooseCity;

    public RecyclerDataAdapterChooseCity(ArrayList<String> cities, IRVOnItemClick onItemClickChooseCity) {
        this.cities = cities;
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
        String text = cities.get(position);

        holder.setTextToTextView(text);
        holder.setOnClickForItem(text);
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cityTextView);
        }

        void setTextToTextView(String text) {
            textView.setText(text);
        }

        void setOnClickForItem(final String text) {
            textView.setOnClickListener(view -> {
                if(onItemClickChooseCity != null) {
                    onItemClickChooseCity.onItemClick(text);
                }
            });
        }
    }

}
