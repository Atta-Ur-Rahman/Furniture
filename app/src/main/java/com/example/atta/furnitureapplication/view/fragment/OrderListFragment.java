package com.example.atta.furnitureapplication.view.fragment;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
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
public class OrderListFragment extends Fragment {

    private View parentView;
    private List<Furniture_Model> furniture_models;
    private Furniture_CURD furniture_curd;


    @BindView(R.id.rv_order_list)RecyclerView rvOrderList;
    public OrderListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_order_list, container, false);

        ButterKnife.bind(this,parentView);
        furniture_curd = new Furniture_CURD(getActivity());
        rvOrderList.setHasFixedSize(true);
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        furniture_models = furniture_curd.ReadAllFurniture();
        FurnitureAdapter adapter = new FurnitureAdapter(furniture_models, getActivity(), furniture_curd);
        rvOrderList.setAdapter(adapter);





        return parentView;
    }

    public static class OrderService extends Service {
        public OrderService() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
