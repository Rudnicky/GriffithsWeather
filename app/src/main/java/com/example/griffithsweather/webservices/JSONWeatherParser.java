package com.example.griffithsweather.webservices;

import com.example.griffithsweather.models.City;
import com.example.griffithsweather.models.Coord;
import com.example.griffithsweather.models.Forecast;
import com.example.griffithsweather.models.Location;
import com.example.griffithsweather.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JSONWeatherParser {
    public static Weather getWeather(String data) throws JSONException  {
        Weather weather = new Weather();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weather.location = loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setHumidity(getInt("humidity", mainObj));
        weather.currentCondition.setPressure(getInt("pressure", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));

        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));
        weather.wind.setDeg(getFloat("deg", wObj));

        // Clouds
        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));

        // We download the icon to show


        return weather;
    }

    // https://openweathermap.org/forecast5
    public static Forecast getForecast(String data) throws JSONException  {

        Forecast forecast = new Forecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);
        JSONObject cityObj = getObject("city", jObj);
        JSONObject coordObj = getObject("coord", cityObj);
        JSONArray listArray = jObj.getJSONArray("list");

        // get coordinates
        Coord coord = new Coord();
        coord.setLat(getFloat("lat", coordObj));
        coord.setLon(getFloat("lon", coordObj));

        // get current city
        City city = new City();
        city.setId(getFloat("id", cityObj));
        city.setName(getString("name", cityObj));
        city.setCountry(getString("country", cityObj));
        city.setPopulation(getFloat("population", cityObj));
        city.setCoord(coord);

        // iterate through forecast list in order
        // to obtain forecast for next five days
        // there's plenty of elements but only because
        // the forecast has been updated every each 3 hours.
        ArrayList<Object> listOfObjects = new ArrayList<>();
        for (int i=0; i<listArray.length(); i++)
        {
            Weather weather = new Weather();
            JSONObject mainObj = getObject("main", listArray.getJSONObject(i));
            weather.currentCondition.setHumidity(getInt("humidity", mainObj));
            weather.currentCondition.setPressure(getInt("pressure", mainObj));
            weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
            weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
            weather.temperature.setTemp(getFloat("temp", mainObj));

            // Wind
            JSONObject wObj = getObject("wind", listArray.getJSONObject(i));
            weather.wind.setSpeed(getFloat("speed", wObj));
            weather.wind.setDeg(getFloat("deg", wObj));

            // Clouds
            JSONObject cObj = getObject("clouds", listArray.getJSONObject(i));
            weather.clouds.setPerc(getInt("all", cObj));

            // Data
            String dObj = getString("dt_txt", listArray.getJSONObject(i));
            weather.setDate(dObj);

            // Add item to the list
            listOfObjects.add(weather);
        }

        forecast.setCity(city);
        forecast.setList(listOfObjects);

        return forecast;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}