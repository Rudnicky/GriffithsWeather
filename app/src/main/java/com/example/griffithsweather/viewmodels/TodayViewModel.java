package com.example.griffithsweather.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;

import com.example.griffithsweather.BR;
import com.example.griffithsweather.R;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.models.Weather;
import com.example.griffithsweather.utilities.TemperatureConverter;
import com.example.griffithsweather.webservices.JSONWeatherParser;
import com.example.griffithsweather.webservices.WeatherHttpClient;

import org.json.JSONException;

public class TodayViewModel extends BaseObservable {

    private IDataManager dataManager;
    private String temperature;
    private String lastUpdate;
    private String city;
    private String rainfall;
    private String windSpeed;
    private String cloudy;
    private int weatherImageResource;

    public TodayViewModel(IDataManager dataManager) {
        this.dataManager = dataManager;
    }

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

    @Bindable
    public int getWeatherImageResource() { return this.weatherImageResource; }
    public void setWeatherImageResource(int weatherImageResource) {
        this.weatherImageResource = weatherImageResource;
        notifyPropertyChanged(BR.weatherImageResource);
    }

    public void getWeatherData() {
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{ city });
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
                setCloudy(String.valueOf(weather.clouds.getPerc()) + "%");
                setWindSpeed(String.valueOf(weather.wind.getSpeed()) + "mph");
                setRainfall(String.valueOf(weather.rain.getAmmount()) + "mm");
                setWeatherImageResource(getWeatherIconThroughCondition(weather.currentCondition.getCondition()));

                // set last updated data
                updateDateTime();
//
//                // set visibility
//                setIsLoaded(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
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
