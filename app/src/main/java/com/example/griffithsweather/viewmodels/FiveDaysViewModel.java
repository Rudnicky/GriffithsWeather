package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.griffithsweather.BR;

public class FiveDaysViewModel extends BaseObservable {
    private String testString;

    @Bindable
    public String getTestString() { return this.testString; }
    public void setTestString(String testString) {
        this.testString = testString;
        notifyPropertyChanged(BR.testString);
    }
}
