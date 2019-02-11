package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.griffithsweather.BR;

public class TodayViewModel extends BaseObservable {
    
    private String temperature;
    private String lastUpdate;
    private String city;
    private String rainfall;
    private String windSpeed;
    private String cloudy;

    @Bindable
    public String getTemperature() { return this.temperature; }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
        notifyPropertyChanged(BR.temperature);
    }

    @Bindable
    public String getLastUpdate() { return this.lastUpdate; }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        notifyPropertyChanged(BR.lastUpdate);
    }

    @Bindable
    public String getCity() { return this.city; }
    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getRainfall() { return this.rainfall; }
    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
        notifyPropertyChanged(BR.rainfall);
    }

    @Bindable
    public String getWindSpeed() { return this.windSpeed; }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
        notifyPropertyChanged(BR.windSpeed);
    }

    @Bindable
    public String getCloudy() { return this.cloudy; }
    public void setCloudy(String cloudy) {
        this.cloudy = cloudy;
        notifyPropertyChanged(BR.cloudy);
    }
}
