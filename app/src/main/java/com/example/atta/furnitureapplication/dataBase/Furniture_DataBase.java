package com.example.atta.furnitureapplication.dataBase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class Furniture_DataBase extends SQLiteOpenHelper {

    private static String DB_NAME = "DB_Furniture";
    private static int DB_VERSION = 1;


    public Furniture_DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE ORDER_ITEM_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,ITEM_NAME,ITEM_PRICE,PHONE_NUMBER,ITEM_IMAGE BLOB)";
        String query1 = "CREATE TABLE ORDER_NAME_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME,PHONE_NUMBER,ORDER_DATE,ORDER_PLACE_DATE,IMAGE_URI BLOB)";
        String query2 = "CREATE TABLE BALANCE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME,ADVANCE_PRICE,PHONE_NUMBER,DATE)";

        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}