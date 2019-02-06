package com.example.griffithsweather.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.ActivityWeatherBinding;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.interfaces.ILocator;
import com.example.griffithsweather.interfaces.IToastMessageListener;
import com.example.griffithsweather.utilities.DataManager;
import com.example.griffithsweather.utilities.Locator;
import com.example.griffithsweather.viewmodels.WeatherViewModel;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;

public class WeatherActivity extends AppCompatActivity implements IToastMessageListener {

    private static final int GPS_REQUEST_FINE_LOCATION_PERMISSION = 0;
    private NetworkStatus networkStatus;
    private WeatherViewModel viewModel;
    private ILocator locator;
    private IDataManager dataManager;
    private String currentCity;
    private Merlin merlin;
    private boolean isWeatherInitialized;

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

    @Override
    protected void onDestroy() {
        if (viewModel != null) {
            viewModel.removeToastMessageListener();
        }
        super.onDestroy();
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setup() {

        // creates dependencies
        this.locator = new Locator();
        this.dataManager = new DataManager();
        this.viewModel = new WeatherViewModel(dataManager);
        this.viewModel.setIsProgressBarVisible(true);
        this.viewModel.setToastMessageListener(this);

        // builds merlin which is external library
        // that observes internet connection and
        // notify if anything has changed.
        buildMerlin();

        // setting up view-model with bindings
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
            if (networkStatus != null && !isWeatherInitialized) {
                this.viewModel.setIsSadCloudVisible(false);
                checkPermissions();
                this.isWeatherInitialized = true;
                this.viewModel.setIsNetworkAvailable(true);
            }
        });

        merlin.registerDisconnectable(() -> {
            this.viewModel.setIsSadCloudVisible(false);
            this.viewModel.setIsProgressBarVisible(false);
            this.viewModel.setIsNetworkAvailable(false);
            this.isWeatherInitialized = false;
        });

        merlin.registerBindable(networkStatus -> {

            // get current networkStatus instance
            this.networkStatus = networkStatus;

            // check whenever internet is available or not
            // if it's available then start the whole process
            // disable sad cloud etc. and change flags
            if (networkStatus != null && networkStatus.isAvailable() && !isWeatherInitialized) {
                this.viewModel.setIsSadCloudVisible(false);
                checkPermissions();
                this.isWeatherInitialized = true;
                this.viewModel.setIsNetworkAvailable(true);
            } else {
                this.viewModel.setIsNetworkAvailable(false);
                this.viewModel.setIsSadCloudVisible(true);
                this.viewModel.setIsProgressBarVisible(false);
                this.isWeatherInitialized = false;
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

    @Override
    public void toastMessageInvoked(String message) {
        setupToastMessage(message);
    }

    private void setupToastMessage(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.cloud_toast, findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
