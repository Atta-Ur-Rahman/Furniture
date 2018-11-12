package com.example.atta.furnitureapplication.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by AttaUrRahman on 7/6/2018.
 */
public class FurnitureRemainderReceiver extends BroadcastReceiver {
    private String TAG = FurnitureRemainderReceiver.class.getSimpleName();


    public FurnitureRemainderReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        context.startService(new Intent(context,RemainderService.class));

    }


}