package com.example.atta.furnitureapplication.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculationFragment extends Fragment {

    private View parentView;
    private String strTotalAmount;
    public CalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_main, container, false);

        strTotalAmount = Utilities.getSharedPreferences(getActivity()).getString("total_price", "");



        return parentView;
    }

}
