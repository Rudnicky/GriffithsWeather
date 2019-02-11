package com.example.griffithsweather.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griffithsweather.R;
import com.example.griffithsweather.databinding.FragmentFiveDaysBinding;
import com.example.griffithsweather.viewmodels.FiveDaysViewModel;


public class FiveDaysFragment extends BaseFragment {

    public FiveDaysFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentFiveDaysBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_five_days, container, false);
        FiveDaysViewModel item = new FiveDaysViewModel();
        item.setTestString("Five Days");
        binding.setViewmodel(item);
        return binding.getRoot();
    }

    @Override
    public void onPermissionAllowed() {

    }
}
