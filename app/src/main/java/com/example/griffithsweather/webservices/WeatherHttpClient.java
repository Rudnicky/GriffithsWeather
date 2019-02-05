package com.example.griffithsweather.webservices;

import com.example.griffithsweather.interfaces.IWeather;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient implements IWeather {

    // documentation for API -> https://openweathermap.org/current

    private final static String API_KEY = "4beacd07fa3127c62b1c45e05c6fb3d9";
    private final static String API_KEY_DIVIDER = "&APPID=";
    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";

    public String getWeatherData(String location) {
        HttpURLConnection connection = null ;
        InputStream inputStream = null;

        try {
            String address = BASE_URL + location + API_KEY_DIVIDER + API_KEY;
            connection = (HttpURLConnection) (new URL(address).openConnection());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            inputStream.close();
            connection.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return null;
    }
}