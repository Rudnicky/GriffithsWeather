package com.example.griffithsweather.interfaces;

import android.content.Context;
import android.location.Location;

public interface ILocator {

    String getCity(Context context);

    Location getDeviceLoc(Context context);
}