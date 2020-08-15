package com.example.weather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.recyclerChooseCity.IRVOnItemClick;
import com.example.weather.R;
import com.example.weather.recyclerChooseCity.RecyclerDataAdapterChooseCity;
import com.example.weather.WeatherActivity;
import com.example.weather.WeatherContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChooseCityFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
    private RecyclerDataAdapterChooseCity adapterChooseCity;
    public ArrayList<String> listCities;

    private boolean isExistWeather;
    private int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fillUpArrayListCity();

        return inflater.inflate(R.layout.choose_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpRecyclerView();
        makeDecorator();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("Current city", 0);
        }

        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("Current city", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void fillUpArrayListCity() {
        listCities = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cities)));
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapterChooseCity = new RecyclerDataAdapterChooseCity(listCities, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterChooseCity);
    }

    private void showWeather() {
        if (isExistWeather) {

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

    @Override
    public void onItemClick(String itemText) {
        currentPosition = listCities.indexOf(itemText);
        showWeather();
    }

    private void makeDecorator() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(
                ContextCompat.getDrawable(getContext(), R.drawable.decorator_item)));
        recyclerView.addItemDecoration(itemDecoration);
    }
}
