package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentCityBinding;
import com.example.griffithsweather.viewmodels.CityViewModel;


public class CityFragment extends Fragment {

    public CityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city, container, false);
        CityViewModel item = new CityViewModel();
        item.setTestString("City");
        binding.setViewmodel(item);
        return binding.getRoot();
    }
}
