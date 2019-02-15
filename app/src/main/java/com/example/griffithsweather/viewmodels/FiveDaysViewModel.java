package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;

import com.example.griffithsweather.BR;
import com.example.griffithsweather.fragments.FiveDaysFragment;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.models.Forecast;
import com.example.griffithsweather.models.Weather;
import com.example.griffithsweather.utilities.TemperatureConverter;
import com.example.griffithsweather.webservices.JSONWeatherParser;
import com.example.griffithsweather.webservices.WeatherHttpClient;

import org.json.JSONException;

public class FiveDaysViewModel extends BaseObservable {

    private IDataManager dataManager;
    private String lastUpdate;
    private String city;

    public FiveDaysViewModel(IDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Bindable
    public String getCity() { return this.city; }
    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getLastUpdate() { return this.lastUpdate; }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        notifyPropertyChanged(BR.lastUpdate);
    }

    public void getForecastWeatherData() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{ city });
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Forecast> {
        @Override
        protected Forecast doInBackground(String... params) {
            Forecast forecast = new Forecast();
            String data = ((new WeatherHttpClient()).getForecastData(params[0]));

            try {

                // gets whole forecast from web services
                forecast = JSONWeatherParser.getForecast(data);

                // convert to celsius
//                TemperatureConverter converter = new TemperatureConverter();
//                float currentTemperature = weather.temperature.getTemp();
//                String tmp = converter.KelvinToCelsius(currentTemperature);
//                String temperature = tmp.substring(0, tmp.indexOf("."));

                // notify ui through bindings
//                setTemperature(temperature + (char) 0x00B0);
//                setCloudy(String.valueOf(weather.clouds.getPerc()) + "%");
//                setWindSpeed(String.valueOf(weather.wind.getSpeed()) + "mph");
//                setRainfall(String.valueOf(weather.rain.getAmmount()) + "mm");
//                setWeatherImageResource(getWeatherIconThroughCondition(weather.currentCondition.getCondition()));

                // set last updated data
                updateDateTime();
//
//                // set visibility
//                setIsLoaded(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;
        }

        @Override
        protected void onPostExecute(Forecast weather) {
            super.onPostExecute(weather);
//            setIsProgressBarVisible(false);
        }
    }

    private void updateDateTime() {
        if (dataManager != null) {
            String currentData = dataManager.getCurrentData();
            setLastUpdate(currentData);
        }
    }
}
