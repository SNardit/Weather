package com.example.weather.fragments.flavor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.example.weather.fragments.WeatherFragment;
import com.example.weather.modelDataBase.City;
import com.example.weather.recyclerChooseCity.IRVOnItemClick;
import com.example.weather.recyclerChooseCity.RecyclerDataAdapterChooseCity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ChooseCityFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
    private TextInputEditText searchCity;
    private MaterialButton searchButton;

    public static final String BROADCAST_ACTION = "com.example.weather.fragments";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private static final String CURRENT_POSITION = "Current position";
    private static final String CURRENT_ID = "currentId";

    private boolean isExistWeather;

    private static long currentPosition = 1;

    public static WeatherSource weatherSource;

    public static long getCurrentPosition() {
        return currentPosition;
    }

    private SignInForGoogle signInForGoogle = new SignInForGoogle();

    private GoogleSignInClient googleSignInClient;
    private final int RC_SIGN_IN = 40404;

    private com.google.android.gms.common.SignInButton buttonSignIn;
    private MaterialButton buttonSingOut;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.choose_city_fragment, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        makeDecorator();
        setUpRecyclerView();
        signInForGoogle.setUpGoogleSignIn();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CURRENT_POSITION, Context.MODE_PRIVATE);
        currentPosition = sharedPreferences.getLong(CURRENT_ID, 1);
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
        if (id == R.id.infoAboutDevelopers) {
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

        signInForGoogle.checkGoogleSignIn();

        isExistWeather = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong(getString(R.string.current_city), 1);
            setUpRecyclerView();
        }

        if (isExistWeather) {
            showWeather();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(getString(R.string.current_city), currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initView(View view) {
        searchCity = view.findViewById(R.id.inputCityText);
        searchButton = view.findViewById(R.id.buttonSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        buttonSignIn = view.findViewById(R.id.sign_in_button);
        buttonSingOut = view.findViewById(R.id.sing_out_button);
        email = view.findViewById(R.id.email);
    }

    public void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        weatherSource = new WeatherSource(weatherDao);

        RecyclerDataAdapterChooseCity adapterChooseCity = new RecyclerDataAdapterChooseCity(weatherSource, this);
        recyclerView.setAdapter(adapterChooseCity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            signInForGoogle.handleSignInResult(task);
        }
    }

    private void setOnInputCityClickListener() {
        searchCity.setOnClickListener(this::actionForChooseCity);
    }

    private void setOnBtnClickListener() {
        searchButton.setOnClickListener(this::actionForChooseCity);
    }

    private void actionForChooseCity(View view) {
        String city = Objects.requireNonNull(searchCity.getText()).toString();
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

            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack("weatherFragment");
                ft.commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(requireActivity(), WeatherActivity.class);
            intent.putExtra(getString(R.string.city_name_string), getWeatherContainer());
            startActivity(intent);
        }
    }

    private WeatherContainer getWeatherContainer() {
        return new WeatherContainer(getCityByCurrentPosition(currentPosition));
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

    public class SignInForGoogle {

        private void setUpGoogleSignIn(){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(requireContext(), gso);

            buttonSignIn.setOnClickListener(v -> signIn()
            );

            buttonSingOut.setOnClickListener(v -> signOut());
        }

        private void signIn() {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void signOut() {
            googleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), task -> {
                        enableSign();
                        updateUI(getString(R.string.email));
                    });
        }

        private void checkGoogleSignIn() {
            signInForGoogle.enableSign();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
            if (account != null) {
                signInForGoogle.disableSign();
                signInForGoogle.updateUI(account.getEmail());
            }
        }

        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                disableSign();
                assert account != null;
                updateUI(account.getEmail());
            } catch (ApiException e) {
                String TAG = "GoogleAuth";
                Log.w(TAG, getString(R.string.sign_in_filed_code), e);
            }
        }

        private void updateUI(String e_mail) {
            email.setText(e_mail);
        }

        private void enableSign(){
            buttonSignIn.setEnabled(true);
            buttonSingOut.setEnabled(false);
        }

        private void disableSign(){
            buttonSignIn.setEnabled(false);
            buttonSingOut.setEnabled(true);
        }
    }

}
