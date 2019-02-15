package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentFiveDaysBinding;
import com.example.griffithsweather.interfaces.IDataManager;
import com.example.griffithsweather.utilities.DataManager;
import com.example.griffithsweather.viewmodels.FiveDaysViewModel;


public class FiveDaysFragment extends BaseFragment {

    private FiveDaysViewModel viewModel;
    private String currentCity;

    public FiveDaysFragment() {
        IDataManager dataManager = new DataManager();
        viewModel = new FiveDaysViewModel(dataManager);
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentFiveDaysBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_five_days, container, false);
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onPermissionAllowed() {
        if (viewModel != null) {
            viewModel.setCity(currentCity);
            viewModel.getForecastWeatherData();
        }
    }
}
