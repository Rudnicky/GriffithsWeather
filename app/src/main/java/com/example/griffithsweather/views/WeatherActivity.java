package com.example.griffithsweather.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.ActivityWeatherBinding;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.interfaces.ILocator;
import com.example.griffithsweather.utilities.DataManager;
import com.example.griffithsweather.utilities.Locator;
import com.example.griffithsweather.viewmodels.WeatherViewModel;
import com.novoda.merlin.Merlin;

public class WeatherActivity extends AppCompatActivity {

    private static final int GPS_REQUEST_FINE_LOCATION_PERMISSION = 0;
    private WeatherViewModel viewModel;
    private ILocator locator;
    private IDataManager dataManager;
    private String currentCity;
    private Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }

    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();
    }

    private void setup() {

        // creates dependencies
        this.locator = new Locator();
        this.dataManager = new DataManager();

        // builds merlin which is external library
        // that observes internet connection and
        // notify if anything has changed.
        buildMerlin();

        // setting up view-model with bindings
        this.viewModel = new WeatherViewModel(dataManager);
        ActivityWeatherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        binding.setViewmodel(viewModel);
        binding.executePendingBindings();
    }

    private void buildMerlin() {
        merlin = new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);

        merlin.registerConnectable(() -> {
            viewModel.setIsInternetAvailable(false);
            checkPermissions();
        });

        merlin.registerDisconnectable(() -> {
            // TODO: think about what needs to be done here, when there's a working app
            // TODO: and somehow we've lost internet connection in the middle of nowhere.
        });

        merlin.registerBindable(networkStatus -> {
            if (networkStatus.isAvailable()) {
                viewModel.setIsInternetAvailable(false);
                checkPermissions();
            } else {
                viewModel.setIsInternetAvailable(true);
            }
        });
    }

    private void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setMessage("Without the location permission I'm not able to find your City and get weather information. Are you sure, you don't want to grant permission?").setPositiveButton("Exit Application", dialogClickListener)
                        .setNegativeButton("Prompt Again", dialogClickListener).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(WeatherActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GPS_REQUEST_FINE_LOCATION_PERMISSION);

                // GPS_REQUEST_FINE_LOCATION_PERMISSION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            // obtain current city
            this.currentCity = locator.getCity(this);

            // notify view-model and update ui
            viewModel.setCityName(this.currentCity);

            // perform weather web-service call
            viewModel.getWeatherData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_REQUEST_FINE_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                    // obtain current city
                    this.currentCity = locator.getCity(this);

                    // notify view-model and update ui
                    viewModel.setCityName(this.currentCity);

                    // perform weather web-service call
                    viewModel.getWeatherData();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    checkPermissions();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // 'Exit Application' btn has been clicked
                    finishAffinity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // 'Prompt Again' btn has been clicked
                    ActivityCompat.requestPermissions(WeatherActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            GPS_REQUEST_FINE_LOCATION_PERMISSION);

                    break;
            }
        }
    };
}
