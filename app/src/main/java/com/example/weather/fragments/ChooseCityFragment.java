package com.example.weather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.WeatherActivity;
import com.example.weather.WeatherContainer;

import java.util.Objects;

public class ChooseCityFragment extends Fragment {
    private ListView citiesListView;
    private TextView emptyTextView;

    private boolean isExistWeather;
    private int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("Current city", 0);
        }

        if (isExistWeather) {
            citiesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showWeather();
        }
    }

    private void initView(View view) {
        citiesListView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
    }

    private void initList() {
        ArrayAdapter adapter=
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.cities,
                android.R.layout.simple_list_item_activated_1);

        citiesListView.setAdapter(adapter);

        citiesListView.setEmptyView(emptyTextView);

        citiesListView.setOnItemClickListener((parent, view, position, id) -> {
            currentPosition = position;
            showWeather();
        });
    }

    private void showWeather() {
        if (isExistWeather) {
            citiesListView.setItemChecked(currentPosition, true);

            WeatherFragment detail = (WeatherFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.weather);

            if (detail == null || detail.getIndex() != currentPosition) {

                detail = WeatherFragment.create(getWeatherContainer());

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack("Some key");
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), WeatherActivity.class);
            intent.putExtra("index", getWeatherContainer());
            startActivity(intent);
        }
    }

    private WeatherContainer getWeatherContainer() {
        String[] cities = getResources().getStringArray(R.array.cities);
        WeatherContainer container = new WeatherContainer();
        container.position = currentPosition;
        container.cityName = cities[currentPosition];
        return container;
    }

}
