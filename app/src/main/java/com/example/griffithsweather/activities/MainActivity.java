package com.example.griffithsweather.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.griffithsweather.R;
import com.example.griffithsweather.adapters.ViewPagerAdapter;
import com.example.griffithsweather.fragments.BaseFragment;
import com.example.griffithsweather.fragments.CityFragment;
import com.example.griffithsweather.fragments.FiveDaysFragment;
import com.example.griffithsweather.fragments.TodayFragment;

import java.io.ObjectInputValidation;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GPS_REQUEST_FINE_LOCATION_PERMISSION = 0;

    private final int[] ICONS = new int[] {
            R.drawable.img_sad_cloud,
            R.drawable.img_snowflake,
            R.drawable.img_storm
    };

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Create instances of fragments
        fragments = new ArrayList<>();
        fragments.add(new TodayFragment());
        fragments.add(new FiveDaysFragment());
        fragments.add(new CityFragment());

        // Adding fragments
        adapter.addFragment(fragments.get(0), "Today");
        adapter.addFragment(fragments.get(1), "5 Days");
        adapter.addFragment(fragments.get(2), "By City");

        // Setting up Adapter
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Adding images
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

            // notify each fragment about granted permission
            for (BaseFragment fragment : fragments) {
                fragment.onPermissionAllowed();
            }
//            // obtain current city
//            this.currentCity = locator.getCity(this);
//
//            // notify view-model and update ui
//            viewModel.setCityName(this.currentCity);
//
//            // perform weather web-service call
//            viewModel.getWeatherData();
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

                    // TODO: this must be done in different way
                    // notify each fragment about granted permission
                    for (BaseFragment fragment : fragments) {
                        fragment.onPermissionAllowed();
                    }

//                    // obtain current city
//                    this.currentCity = locator.getCity(this);
//
//                    // notify view-model and update ui
//                    viewModel.setCityName(this.currentCity);
//
//                    // perform weather web-service call
//                    viewModel.getWeatherData();
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
