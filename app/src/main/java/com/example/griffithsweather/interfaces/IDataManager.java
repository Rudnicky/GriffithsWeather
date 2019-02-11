package com.example.griffithsweather.interfaces;


import java.io.Serializable;

public interface IDataManager extends Serializable {

    String getCurrentData();

    String getDayNightCycle();
}