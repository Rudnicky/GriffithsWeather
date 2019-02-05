package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;

import com.example.griffithsweather.BR;
import com.example.griffithsweather.R;
import com.example.griffithsweather.converters.TemperatureConverter;
import com.example.griffithsweather.models.Weather;
import com.example.griffithsweather.utilities.DataManager;
import com.example.griffithsweather.webservices.JSONWeatherParser;
import com.example.griffithsweather.webservices.WeatherHttpClient;

import org.json.JSONException;

public class WeatherViewModel extends BaseObservable {

    private String cityName;
    private String temperature;
    private String lastUpdate;
    private String rain;
    private String clouds;
    private String wind;
    private int weatherImageResource;
    private DataManager dataManager;

    public WeatherViewModel() {
        this.dataManager = new DataManager();
    }

    @Bindable
    public String getCityName() {
        return this.cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
    }

    @Bindable
    public String getTemperature() { return this.temperature; }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
        notifyPropertyChanged(BR.temperature);
    }

    @Bindable
    public String getRain() { return this.rain; }
    public void setRain(String rain) {
        this.rain = rain;
        notifyPropertyChanged(BR.rain);
    }

    @Bindable
    public String getClouds() { return this.clouds; }
    public void setClouds(String clouds) {
        this.clouds = clouds;
        notifyPropertyChanged(BR.clouds);
    }

    @Bindable
    public String getWind() { return this.wind; }
    public void setWind(String wind) {
        this.wind = wind;
        notifyPropertyChanged(BR.wind);
    }

    @Bindable
    public int getWeatherImageResource() { return this.weatherImageResource; }
    public void setWeatherImageResource(int weatherImageResource) {
        this.weatherImageResource = weatherImageResource;
        notifyPropertyChanged(BR.weatherImageResource);
    }

    @Bindable
    public String getLastUpdate() {return this.lastUpdate; }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        notifyPropertyChanged(BR.lastUpdate);
    }

    public void updateDateTime() {
        String currentData = this.dataManager.getCurrentData();
        setLastUpdate(currentData);
    }

    public void getWeatherData() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{ cityName });
    }

    public void onRefreshButtonClicked() {
        getWeatherData();
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {

                // get weather data
                weather = JSONWeatherParser.getWeather(data);

                // convert to celsius
                TemperatureConverter converter = new TemperatureConverter();
                float currentTemperature = weather.temperature.getTemp();
                String temperature = converter.KelvinToCelsius(currentTemperature);

                // notify ui through bindings
                setTemperature(temperature + (char) 0x00B0);
                setClouds(String.valueOf(weather.clouds.getPerc()) + "%");
                setWind(String.valueOf(weather.wind.getSpeed()) + "mph");
                setRain(String.valueOf(weather.rain.getAmmount()) + "mm");
                setWeatherImageResource(getWeatherIconThroughCondition(weather.currentCondition.getCondition()));

                // set last updated data
                updateDateTime();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }
    }

    private int getWeatherIconThroughCondition(String condition) {
        String cycle = dataManager.getDayNightCycle();

        switch (condition) {
            case "Clouds":
                if (cycle == "dayCycle") {
                    return R.drawable.img_partly_cloudly;
                } else if (cycle == "nightCycle") {
                    return R.drawable.img_cloudy_night;
                }
                return R.drawable.img_partly_cloudly;
            case "Rain":
                return R.drawable.img_rainy_day;
            case "Clear":
                if (cycle == "dayCycle") {
                    return R.drawable.img_sun;
                } else if (cycle == "nightCycle") {
                    return R.drawable.img_clear_night;
                }
                return R.drawable.img_sun;
            case "Atmosphere":
                return R.drawable.img_atmosphere;
            case "Snow":
                return R.drawable.img_snowflake;
            case "Drizzle":
                return R.drawable.img_drizzle;
            case "Thunderstorm":
                return R.drawable.img_storm;
            default:
                return R.drawable.img_sun;
        }
    }
}
