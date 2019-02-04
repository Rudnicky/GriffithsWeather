package com.example.griffithsweather.locator;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Locator implements ILocator {

    private Context m_currentContext;
    private double m_Longitude;
    private double m_Latitude;

    public Locator(Context context) {
        this.m_currentContext = context;
        getCurrentLocation();
    }

    @Override
    public String getCity() {
        try {
            Geocoder geocoder = new Geocoder(m_currentContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(m_Latitude, m_Longitude, 1);
            return addresses.get(0).getLocality();
        } catch (IOException e) {}

        return null;
    }

    private void getCurrentLocation() {
        LocationManager lm = (LocationManager) m_currentContext.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(m_currentContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        this.m_Longitude = location.getLongitude();
        this.m_Latitude = location.getLatitude();
    }
}
