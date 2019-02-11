package com.example.griffithsweather.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.example.griffithsweather.interfaces.ILocator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Locator implements ILocator {

    @Override
    public String getCity(Context context) {

        if (context != null) {
            // check if permissions is granted
            // if it's not returns null
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return null;
            }

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // gets city by longitude and latitude
            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                return addresses.get(0).getLocality();
            } catch (IOException e) {}
        }
        return null;
    }
}