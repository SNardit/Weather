package com.example.weather.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.AboutDevelopers;
import com.example.weather.App;
import com.example.weather.R;
import com.example.weather.WeatherActivity;
import com.example.weather.WeatherContainer;
import com.example.weather.WeatherSource;
import com.example.weather.dao.WeatherDao;
import com.example.weather.modelDataBase.City;
import com.example.weather.recyclerChooseCity.IRVOnItemClick;
import com.example.weather.recyclerChooseCity.RecyclerDataAdapterChooseCity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ChooseCityFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
    private RecyclerDataAdapterChooseCity adapterChooseCity;
    private TextInputEditText searchCity;
    private MaterialButton searchButton;

    private boolean isExistWeather;

    private static long currentPosition = 1;
    private String city;

    public static WeatherSource weatherSource;

    public static long getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.choose_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        makeDecorator();
        setUpRecyclerView();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Current position", Context.MODE_PRIVATE);
        currentPosition = sharedPreferences.getLong("currentId", 1);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.actSearch);

        menu.findItem(R.id.infoAboutCity).setVisible(false);

        final SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                actionForSearchCityFromMainMenu(query, searchText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.infoAboutDevelopers:
                setOnBtnInfoDevelop();
                return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setOnInputCityClickListener();
        setOnBtnClickListener();

        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("Current city", 1);
            setUpRecyclerView();
        }

        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("Current city", currentPosition);
        super.onSaveInstanceState(outState);
    }


    private void initView(View view) {
        searchCity = view.findViewById(R.id.inputCityText);
        searchButton = view.findViewById(R.id.buttonSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    public void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        weatherSource = new WeatherSource(weatherDao);

        adapterChooseCity = new RecyclerDataAdapterChooseCity(weatherSource, this::onItemClick);
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

            boolean isCityExist = false;
            for (int i = 0; i < weatherSource.getCountCities(); i++) {
                isCityExist = weatherSource.getCities().get(i).cityName.equals(city);
                if (isCityExist) break;
            }
            if (!isCityExist) {
                City cityName= new City();
                cityName.cityName = city;
                weatherSource.addCity(cityName);
            }
            currentPosition = getPosition(city);
            showWeather();
        }
    }

    private void actionForSearchCityFromMainMenu(String city, View search) {
        if (!city.matches("")) {

            boolean isCityExist = false;
            for (int i = 0; i < weatherSource.getCountCities(); i++) {
                isCityExist = weatherSource.getCities().get(i).cityName.equals(city);
                if (isCityExist) break;
            }
            if (isCityExist) {
                currentPosition = getPosition(city);
                showWeather();
            } else {
                Snackbar.make(search, city + getString(R.string.NoteAboutCityDontExistInHistory), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }
    }

    private void showWeather() {

        if (isExistWeather) {

                WeatherFragment detail = WeatherFragment.create(getWeatherContainer());
                setUpRecyclerView();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack("Some key");
                ft.commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(requireActivity(), WeatherActivity.class);
            intent.putExtra("cityName", getWeatherContainer());
            startActivity(intent);
        }
    }

    private WeatherContainer getWeatherContainer() {
        WeatherContainer container = new WeatherContainer(getCityByCurrentPosition(currentPosition));
        return container;
    }

    @Override
    public void onItemClick(String city) {
        currentPosition = getPosition(city);
        showWeather();
    }

    private long getPosition(String city) {
        return weatherSource.getIdByCityName(city);
    }

    private String getCityByCurrentPosition(long id) {
        return weatherSource.getCityNameById(id);
    }

    private void makeDecorator() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(
                ContextCompat.getDrawable(requireContext(), R.drawable.decorator_item)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setOnBtnInfoDevelop() {
        Intent intent = new Intent();
        intent.setClass(requireActivity(), AboutDevelopers.class);
        startActivity(intent);
    }

}
