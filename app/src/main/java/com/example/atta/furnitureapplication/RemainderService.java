package com.example.atta.furnitureapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RemainderService extends Service {
    private Furniture_CURD furniture_curd;
    private boolean notificationtime = true;

    public RemainderService() {

//

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        furniture_curd = new Furniture_CURD(getApplicationContext());

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                String currentDateandTime = sdf.format(new Date());
                String strNoificationTime = Utilities.getSharedPreferences(getApplicationContext()).getString("notification_time", "0900");

                if (currentDateandTime.equals(strNoificationTime)) {


                    boolean isNotificationSet = true;
                    furniture_curd.ReadByDate(null, isNotificationSet);
                    notificationtime = false;
                }

            handler.postDelayed(this,50000);
            }
        };
        handler.postDelayed(runnable, 1000);

        return super.onStartCommand(intent, flags, startId);
    }

}
