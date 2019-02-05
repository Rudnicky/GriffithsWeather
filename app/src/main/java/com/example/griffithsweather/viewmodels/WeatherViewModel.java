package com.example.griffithsweather.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<String> cityName;
    public MutableLiveData<String> getCityName() {
        if (cityName == null) {
            cityName = new MutableLiveData<String>();
        }
        return cityName;
    }

    public void onCityNameObtained(String cityName) {
        
    }
}
