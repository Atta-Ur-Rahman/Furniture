package com.example.atta.furnitureapplication;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int i = 1;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int mHour, mMinute;

    private Dialog AlarmDialog;

    private Furniture_CURD furniture_curd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        furniture_curd = new Furniture_CURD(this);
        if (Utilities.getSharedPreferences(MainActivity.this).getBoolean("start_services", true)) {

            startService(new Intent(MainActivity.this, RemainderService.class));
            Utilities.putValueInEditor(this).putBoolean("start_services", false).commit();

        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        if (Utilities.getSharedPreferences(MainActivity.this).getBoolean("new_order_update", false)) {
            viewPager.setCurrentItem(1);
            Utilities.putValueInEditor(MainActivity.this).putBoolean("new_order_update", false).commit();
        }


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LastFragment(), "ACTIVE ORDERS");
        adapter.addFragment(new OrderListFragment(), "ALL ORDERS");
        adapter.addFragment(new OrderFragment(), "ADD ORDER");
        viewPager.setAdapter(adapter);


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        // remove the following flag for version < API 19
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_order:

                break;
            case R.id.action_change_notification_day:
                alramDaydialog();
                break;
            case R.id.action_total_balance:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        furniture_curd.CheckAllBalence(MainActivity.this);

                        String strBalence = Utilities.getSharedPreferences(MainActivity.this).getString("total_balance", "");
                        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(MainActivity.this);
                        pictureDialog.setTitle("Total Balance");
                        String[] pictureDialogItems = {"\t RS: " + strBalence};
                        pictureDialog.setItems(pictureDialogItems, null);
                        pictureDialog.show();
                    }
                }, 500);
                break;
            case R.id.action_change_notification_time:


                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String strTime = String.valueOf(hourOfDay + "" + minute);


                                DecimalFormat format = new DecimalFormat("#0000");
                                String strNotificationTime = format.format(Long.parseLong(strTime));
                                Utilities.putValueInEditor(MainActivity.this).putString("notification_time", strNotificationTime).commit();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


        }

        return true;
    }


    private void alramDaydialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.alram_day_layout, null);
        dialogView.setBackgroundResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        AlarmDialog = dialogBuilder.create();
        final EditText etInput = dialogView.findViewById(R.id.et_input_day);
        final Button btnDone = dialogView.findViewById(R.id.btn_inputday);

        int alarmDay = Utilities.getSharedPreferences(MainActivity.this).getInt("alarm_day", 0);

        etInput.setText(String.valueOf(alarmDay));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strInput = etInput.getText().toString();
                if (strInput.equals("")) {
                    etInput.setError("enter day");
                } else {
                    Utilities.putValueInEditor(MainActivity.this).putInt("alarm_day", Integer.parseInt(strInput)).commit();
                    AlarmDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Succsessful", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                }
            }
        });

        AlarmDialog.show();
        AlarmDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {


                return false;
            }
        });
    }
}




















































/*





  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
//
//        try {
//            Date date1 = simpleDateFormat.parse("2-10-2013");
//            Date date2 = simpleDateFormat.parse("13-11-2013");
//
//            String string = Utilities.printDifference(date1, date2);
//
//            Log.d("string",String .valueOf(date1)+"   "+String .valueOf(date2));
//            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
////        i++;
////        Utilities.createNotification(MainActivity.this, "fffff", "ffdslkfja", null, i);
////        Toast.makeText(MainActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
//


    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    Date date = new Date();
    String dateOfDay = simpleDateFormat.format(date);

    String timeofday = android.text.format.DateFormat.format("HH:mm:ss", new Date().getTime()).toString();

    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        try {
                Date date1 = dateFormat.parse("01 07 2018" + " " + "10:12:56");
                Date date2 = dateFormat.parse(dateOfDay + " " + timeofday);

                printDifference(date1, date2);

                } catch (ParseException e) {
                e.printStackTrace();
                }

                }


@SuppressLint("SetTextI18n")
private void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

//        Toast.makeText(MainActivity.this, elapsedDays + "     jgjh " + elapsedHours + " " + elapsedMinutes + " " + elapsedSeconds, Toast.LENGTH_SHORT).show();
        }

*/

