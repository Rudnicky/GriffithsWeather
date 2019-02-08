package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentTodayBinding;
import com.example.griffithsweather.viewmodels.TodayViewModel;


public class TodayFragment extends Fragment {

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTodayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false);
        TodayViewModel item = new TodayViewModel();
        item.setTestString("Today");
        binding.setViewmodel(item);
        return binding.getRoot();
    }
}