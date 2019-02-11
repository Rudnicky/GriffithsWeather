package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentTodayBinding;
import com.example.griffithsweather.viewmodels.TodayViewModel;


public class TodayFragment extends BaseFragment {

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTodayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false);
        TodayViewModel item = new TodayViewModel();
        item.setTemperature("Today");
        binding.setViewmodel(item);
        return binding.getRoot();
    }

    @Override
    public void onPermissionAllowed() {

    }
}
