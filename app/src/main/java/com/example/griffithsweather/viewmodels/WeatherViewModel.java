package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;

import com.example.griffithsweather.BR;
import com.example.griffithsweather.R;
import com.example.griffithsweather.converters.TemperatureConverter;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.interfaces.IToastMessageListener;
import com.example.griffithsweather.models.Weather;
import com.example.griffithsweather.webservices.JSONWeatherParser;
import com.example.griffithsweather.webservices.WeatherHttpClient;

import org.json.JSONException;

public class WeatherViewModel extends BaseObservable {

    private static final long MINIMUM_REFRESH_DELAY_MS = 1000;
    private String cityName;
    private String temperature;
    private String lastUpdate;
    private String rain;
    private String clouds;
    private String wind;
    private boolean isLoaded;
    private boolean isSadCloudVisible;
    private boolean isProgressBarVisible;
    private boolean isNetworkAvailable;
    private int weatherImageResource;
    private long lastRefreshClick;
    private IDataManager dataManager;
    private IToastMessageListener messageListener;

    public WeatherViewModel(IDataManager dataManager) {
        this.dataManager = dataManager;
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
    public boolean getIsLoaded() { return this.isLoaded; }
    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
        notifyPropertyChanged(BR.isLoaded);
    }

    @Bindable
    public boolean getIsSadCloudVisible() { return this.isSadCloudVisible; }
    public void setIsSadCloudVisible(boolean isSadCloudVisible) {
        this.isSadCloudVisible = isSadCloudVisible;
        notifyPropertyChanged(BR.isSadCloudVisible);
    }

    @Bindable
    public boolean getIsProgressBarVisible() { return this.isProgressBarVisible; }
    public void setIsProgressBarVisible(boolean isProgressBarVisible) {
        this.isProgressBarVisible = isProgressBarVisible;
        notifyPropertyChanged(BR.isProgressBarVisible);
    }

    @Bindable
    public boolean getIsNetworkAvailable() { return this.isNetworkAvailable; }
    public void setIsNetworkAvailable(boolean isNetworkAvailable) {
        this.isNetworkAvailable = isNetworkAvailable;
        notifyPropertyChanged(BR.isNetworkAvailable);
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
        long lastClick = lastRefreshClick;
        long now = System.currentTimeMillis();
        lastRefreshClick = now;

        if (now - lastClick < MINIMUM_REFRESH_DELAY_MS) {
            // way to fast. Ignore it.
        } else {
            if (isNetworkAvailable) {
                setIsProgressBarVisible(true);
                getWeatherData();
                if (messageListener != null) {
                    messageListener.toastMessageInvoked("Weather updated!");
                }
            } else {
                if (messageListener != null) {
                    messageListener.toastMessageInvoked("Please turn on your internet.");
                }
            }
        }
    }

    public void setToastMessageListener(IToastMessageListener listener) {
        this.messageListener = listener;
    }

    public void removeToastMessageListener() {
        this.messageListener = null;
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
                String tmp = converter.KelvinToCelsius(currentTemperature);
                String temperature = tmp.substring(0, tmp.indexOf("."));

                // notify ui through bindings
                setTemperature(temperature + (char) 0x00B0);
                setClouds(String.valueOf(weather.clouds.getPerc()) + "%");
                setWind(String.valueOf(weather.wind.getSpeed()) + "mph");
                setRain(String.valueOf(weather.rain.getAmmount()) + "mm");
                setWeatherImageResource(getWeatherIconThroughCondition(weather.currentCondition.getCondition()));

                // set last updated data
                updateDateTime();

                // set visibility
                setIsLoaded(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            setIsProgressBarVisible(false);
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
