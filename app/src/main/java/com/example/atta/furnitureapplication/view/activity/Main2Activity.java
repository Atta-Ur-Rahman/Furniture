package com.example.atta.furnitureapplication.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.view.fragment.AddItemsFragment;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Utilities.connectFragment(this, new AddItemsFragment());
    }
}
