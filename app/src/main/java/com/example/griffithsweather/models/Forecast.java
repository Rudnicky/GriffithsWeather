package com.example.griffithsweather.models;

import java.util.ArrayList;
import java.util.List;

public class Forecast {
    private String cod;
    private float message;
    private float cnt;
    ArrayList< Object > list = new ArrayList < Object > ();
    City CityObject;


    // Getter Methods

    public String getCod() {
        return cod;
    }

    public float getMessage() {
        return message;
    }

    public float getCnt() {
        return cnt;
    }

    public City getCity() {
        return CityObject;
    }

    // Setter Methods

    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setMessage(float message) {
        this.message = message;
    }

    public void setCnt(float cnt) {
        this.cnt = cnt;
    }

    public void setCity(City cityObject) {
        this.CityObject = cityObject;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    public ArrayList<Object> getList() {
        return this.list;
    }
}
