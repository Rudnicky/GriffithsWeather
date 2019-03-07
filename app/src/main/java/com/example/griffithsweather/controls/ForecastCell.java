package com.example.griffithsweather.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.griffithsweather.R;

public class ForecastCell extends LinearLayout {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ForecastCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        // inflate created layout
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forecastcell_layout, this, true);

        // get custom attributes
        // obtain drawable from particular attribute
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.forecast);
        Drawable drawable = array.getDrawable(R.styleable.forecast_src);
        String str = array.getString(R.styleable.forecast_txt);

        // I'm not sure why recycle is needed here
        array.recycle();

        // apply new given source from xml file through custom attribute
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(drawable);
        // wtf
        TextView txtView = findViewById(R.id.textView);
        txtView.setText(str);
    }
}