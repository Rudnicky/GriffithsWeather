package com.example.griffithsweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // go back to default theme after splash screen
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
    }
}
