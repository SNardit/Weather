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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChooseCityFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
    private RecyclerDataAdapterChooseCity adapterChooseCity;
    private ArrayList<String> listCities;
    private TextInputEditText searchCity;
    private MaterialButton searchButton;

    private boolean isExistWeather;
    private int currentPosition = 0;
    private String city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fillUpArrayListCity();

        return inflater.inflate(R.layout.choose_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        makeDecorator();
        setUpRecyclerView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setOnInputCityClickListener();
        setOnBtnClickListener();


        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("Current city", 0);
            listCities = savedInstanceState.getStringArrayList("Cities list");
            setUpRecyclerView();

        }

        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList("Cities list", listCities);
        outState.putInt("Current city", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void fillUpArrayListCity() {
        listCities = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cities)));
    }

    private void initView(View view) {
        searchCity = view.findViewById(R.id.inputCityText);
        searchButton = view.findViewById(R.id.buttonSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapterChooseCity = new RecyclerDataAdapterChooseCity(listCities, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterChooseCity);
    }

    private void setOnInputCityClickListener() {
        searchCity.setOnClickListener(this::actionForChooseCity);
    }

    private void setOnBtnClickListener() {
        searchButton.setOnClickListener(this::actionForChooseCity);
    }

    private void actionForChooseCity(View view) {
        city = Objects.requireNonNull(searchCity.getText()).toString();
        if (!city.matches("")) {

            Snackbar.make(view, "Choose entered city?", Snackbar.LENGTH_LONG).
                    setAction("Choose", (v) -> {
                        boolean isCityExist = false;
                        for (int i = 0; i < listCities.size(); i++) {
                            isCityExist = listCities.get(i).equals(city);
                            if (isCityExist) break;
                        }
                        if (!isCityExist) {
                        listCities.add(city);
                        }
                        currentPosition = listCities.indexOf(city);
                        showWeather();
                }).show();
        }
    }

    private void showWeather() {
        if (isExistWeather) {

            WeatherFragment detail = (WeatherFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.weather);

            if (detail == null || detail.getIndex() != currentPosition) {

                detail = WeatherFragment.create(getWeatherContainer());
                setUpRecyclerView();

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
        WeatherContainer container = new WeatherContainer();
        container.position = currentPosition;
        container.cityName = listCities.get(currentPosition);
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
