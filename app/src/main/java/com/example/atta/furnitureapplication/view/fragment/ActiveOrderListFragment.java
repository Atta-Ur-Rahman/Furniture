package com.example.atta.furnitureapplication.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atta.furnitureapplication.adapter.FurnitureAdapter;
import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;
import com.example.atta.furnitureapplication.adapter.Furniture_Model;
import com.example.atta.furnitureapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveOrderListFragment extends Fragment {


    private View parentView;
    private List<Furniture_Model> furniture_models;
    private Furniture_CURD furniture_curd;



    @BindView(R.id.pager)ViewPager viewPager;
    @BindView(R.id.rv_order_last_list)RecyclerView rvOrderList;

    public ActiveOrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_last, container, false);
        ButterKnife.bind(this,parentView);
        furniture_curd = new Furniture_CURD(getActivity());
        rvOrderList.setHasFixedSize(true);
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        boolean isNotificationSet = false;

        furniture_models = furniture_curd.ReadByDate(parentView,isNotificationSet);
        FurnitureAdapter adapter = new FurnitureAdapter(furniture_models, getActivity(), furniture_curd);
        rvOrderList.setAdapter(adapter);






        return parentView;
    }

}
