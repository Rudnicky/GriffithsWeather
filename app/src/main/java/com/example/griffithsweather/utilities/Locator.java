package com.example.griffithsweather.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.griffithsweather.interfaces.ILocator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Locator implements ILocator {

    @Override
    public String getCity(Context context) {

        if (context != null) {

            Location location = getDeviceLoc(context);
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    return addresses.get(0).getLocality();
                } catch (IOException e) { }
            }
        }
        return null;
    }

    public Location getDeviceLoc(Context context) {

        // always check if context is NOT null
        if (context != null) {

            // get system service
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // check if permission is granted
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return null;
            }

            // if location manager is NOT equal null,
            // let's try to get location by different possibilities.
            if (lm != null) {
                Location locationByNetwork = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location locationByGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationByPassiveProvider = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if (locationByNetwork != null) {
                    return locationByNetwork;
                } else if (locationByGPS != null) {
                    return locationByGPS;
                } else if (locationByPassiveProvider != null) {
                    return locationByPassiveProvider;
                }
            }
        }
        return null;
    }
}