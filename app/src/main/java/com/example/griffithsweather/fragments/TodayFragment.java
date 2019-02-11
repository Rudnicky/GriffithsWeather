package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentTodayBinding;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.utilities.DataManager;
import com.example.griffithsweather.viewmodels.TodayViewModel;


public class TodayFragment extends BaseFragment {

    private TodayViewModel viewModel;
    private String currentCity;

    public TodayFragment() {
        IDataManager dataManager = new DataManager();
        viewModel = new TodayViewModel(dataManager);
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTodayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false);
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }


    @Override
    public void onPermissionAllowed() {
        if (viewModel != null) {
            viewModel.setCity(currentCity);
            viewModel.getWeatherData();
        }
    }
}
