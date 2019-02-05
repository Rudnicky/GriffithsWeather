package com.example.griffithsweather.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateManager {

    public String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }
}
