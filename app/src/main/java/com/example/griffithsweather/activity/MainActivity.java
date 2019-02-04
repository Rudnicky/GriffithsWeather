package com.example.griffithsweather.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.ActivityMainBinding;
import com.example.griffithsweather.locator.ILocator;
import com.example.griffithsweather.locator.Locator;
import com.example.griffithsweather.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int GPS_REQUEST_FINE_LOCATION_PERMISSION = 0;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting up view model with bindings
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        // check current permissions
        checkPermissions();
    }

    private void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Without the location permission I'm not able to find your City and get weather information. Are you sure, you don't want to grant permission?").setPositiveButton("Exit Application", dialogClickListener)
                        .setNegativeButton("Prompt Again", dialogClickListener).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GPS_REQUEST_FINE_LOCATION_PERMISSION);

                // GPS_REQUEST_FINE_LOCATION_PERMISSION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            // do whatever you have to do.
            ILocator locator = new Locator(MainActivity.this);
            viewModel.findCurrentCity(locator);
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
                    // contacts-related task you need to do.
                    ILocator locator = new Locator(MainActivity.this);
                    viewModel.findCurrentCity(locator);
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
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            GPS_REQUEST_FINE_LOCATION_PERMISSION);

                    break;
            }
        }
    };
}
