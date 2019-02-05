package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.griffithsweather.BR;
import com.example.griffithsweather.converters.TemperatureConverter;
import com.example.griffithsweather.models.Weather;
import com.example.griffithsweather.webservices.JSONWeatherParser;
import com.example.griffithsweather.webservices.WeatherHttpClient;

import org.json.JSONException;

public class WeatherViewModel extends BaseObservable {

    private String cityName;
    private String temperature;

    @Bindable
    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        notifyPropertyChanged(BR.cityName);
    }

    @Bindable
    public String getTemperature() {return this.temperature; }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
        notifyPropertyChanged(BR.temperature);
    }

    public void getWeatherData() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{ cityName });
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
                setTemperature(temperature);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }
    }
}
