package com.example.griffithsweather.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataManager {

    public String getCurrentData() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }

    public String getDayNightCycle() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date_from = formatter.parse("00:00");
            Date date_to = formatter.parse("08:00");
            Date dateNow = formatter.parse(formatter.format(new Date()));
            String check = "dayCycle";
            if (date_from.before(date_to)) { // they are on the same day
                if (dateNow.after(date_from) && dateNow.before(date_to)) {
                    check = "nightCycle";
                } else {
                    check = "dayCycle";
                }
            } else { // interval crossing midnight
                if (dateNow.before(date_to) || dateNow.after(date_from)) {
                    check = "nightCycle";
                } else {
                    check = "dayCycle";
                }
            }
            return check;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
