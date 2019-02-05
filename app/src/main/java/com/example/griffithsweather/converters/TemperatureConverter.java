package com.example.griffithsweather.converters;

public class TemperatureConverter {

    public String FahrenheitToCelsius(float fahrenheit) {
        float celsius = ((fahrenheit - 32) * 5) / 9;
        return String.valueOf(celsius);
    }

    public String KelvinToCelsius(float kelvin) {
        float celsius = kelvin - 273.15F;
        return String.valueOf(celsius);
    }
}
