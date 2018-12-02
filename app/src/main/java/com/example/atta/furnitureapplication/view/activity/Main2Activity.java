package com.example.atta.furnitureapplication.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.view.fragment.AddItemsFragment;
import com.example.atta.furnitureapplication.view.fragment.AddOrderFragment;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (Utilities.getSharedPreferences(this).getBoolean("menu_add", false)) {

            Utilities.withOutBackStackConnectFragment(this, new AddOrderFragment());
            Utilities.putValueInEditor(this).putBoolean("menu_add", false).commit();
        } else {
            Utilities.connectFragment(this, new AddItemsFragment());
        }

    }
}
