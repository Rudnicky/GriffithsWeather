package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.griffithsweather.BR;

public class WeatherViewModel extends BaseObservable {

    private String cityName;

    @Bindable
    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
    }
}
