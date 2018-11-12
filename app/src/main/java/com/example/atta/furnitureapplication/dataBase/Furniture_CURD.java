package com.example.atta.furnitureapplication.dataBase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.atta.furnitureapplication.adapter.Furniture_Model;
import com.example.atta.furnitureapplication.view.activity.MainActivity;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.view.fragment.AddItemsFragment;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Furniture_CURD {


    SQLiteDatabase sqLiteDatabase;
    private Context context;
    private int i = 1;

    public Furniture_CURD(Context context) {
        Furniture_DataBase contact_dataBase = new Furniture_DataBase(context);
        sqLiteDatabase = contact_dataBase.getWritableDatabase();
        this.context = context;
    }

    public void insertOrder(String strName, String strPhoneNumber, String strOrderDate, String strOrderPlaceDate, String strImageUri) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_NAME_TABLE WHERE PHONE_NUMBER = '" + strPhoneNumber + "' ", null);

        if (cursor.moveToFirst()) {

//            Toast.makeText(context, "Already Exist ", Toast.LENGTH_SHORT).show();

        } else {
            ///here insert new student in database
            ContentValues values = new ContentValues();
            values.put("NAME", strName);
            values.put("PHONE_NUMBER", strPhoneNumber);
            values.put("ORDER_DATE", strOrderDate);
            values.put("ORDER_PLACE_DATE", strOrderPlaceDate);
            values.put("IMAGE_URI", strImageUri);

            Utilities.putValueInEditor(context).putBoolean("new_order_update",true).commit();
            ((Activity) context).finish();
            context.startActivity(new Intent(context, MainActivity.class));
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
            sqLiteDatabase.insert("ORDER_NAME_TABLE", null, values);

        }
    }

    public boolean checkOrderExist(String strPhoneNumber) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_NAME_TABLE WHERE PHONE_NUMBER = '" + strPhoneNumber + "' ", null);
        boolean isCheckOrderPhoneNumber = true;
        if (cursor.moveToFirst()) {

            Toast.makeText(context, "Already Exist ", Toast.LENGTH_SHORT).show();
            isCheckOrderPhoneNumber = false;

        }
        return isCheckOrderPhoneNumber;
    }


    public void insertItemsExist(String strOrderItemImage, String strOrderItemName, String strItemPrice, String strOrderPhoneNumber) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_ITEM_TABLE WHERE PHONE_NUMBER = '" + strOrderPhoneNumber + "' AND ITEM_NAME= '" + strOrderItemName + "'", null);

        if (cursor.moveToFirst()) {

//            Toast.makeText(context, "Already Exist This Name", Toast.LENGTH_SHORT).show();

        } else {
            ///here insert new student in database
            ContentValues values = new ContentValues();
            values.put("ITEM_NAME", strOrderItemName);
            values.put("PHONE_NUMBER", strOrderPhoneNumber);
            values.put("ITEM_PRICE", strItemPrice);
            values.put("ITEM_IMAGE", strOrderItemImage);

            sqLiteDatabase.insert("ORDER_ITEM_TABLE", null, values);
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
            Utilities.withOutBackStackConnectFragment(context, new AddItemsFragment());

        }
    }

    public boolean checkInsertItem(String strOrderItemName, String strOrderPhoneNumber) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_ITEM_TABLE WHERE PHONE_NUMBER = '" + strOrderPhoneNumber + "' AND ITEM_NAME= '" + strOrderItemName + "'", null);

        boolean isCheckInsertItemName = true;

        if (cursor.moveToFirst()) {

            Toast.makeText(context, "Already Exist This Name", Toast.LENGTH_SHORT).show();
            isCheckInsertItemName = false;

        }
        return isCheckInsertItemName;
    }


    public void Delete(String strPhone, Context context) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_NAME_TABLE WHERE PHONE_NUMBER = '" + strPhone + "' ", null);

        if (cursor.moveToFirst()) {
            this.sqLiteDatabase.delete("ORDER_NAME_TABLE", "PHONE_NUMBER = '" + strPhone + "'", null);
            this.sqLiteDatabase.delete("ORDER_ITEM_TABLE", "PHONE_NUMBER = '" + strPhone + "'", null);


            String strImageUri = (cursor.getString(cursor.getColumnIndex("IMAGE_URI")));

            Toast.makeText(context, strImageUri, Toast.LENGTH_SHORT).show();

            Uri image_uri = Uri.parse(strImageUri);
            File file = new File(image_uri.getPath());
            file.delete();
            if (file.exists()) {
                try {
                    file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    context.getApplicationContext().deleteFile(file.getName());
                }
            }

            Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
            context.startActivity(new Intent(context, MainActivity.class));


        } else {

//            Toast.makeText(context, "No Exist", Toast.LENGTH_SHORT).show();
        }


    }

    public void DeleteItem(String strId, Context context) {

        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_ITEM_TABLE WHERE ID = '" + strId + "'", null);


        if (cursor.moveToFirst()) {


            this.sqLiteDatabase.delete("ORDER_ITEM_TABLE", "ID = '" + strId + "'", null);

            Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();

            Utilities.withOutBackStackConnectFragment(context, new AddItemsFragment());

        } else {

            Toast.makeText(context, "No Exist", Toast.LENGTH_SHORT).show();
        }


    }


    @SuppressLint("ResourceAsColor")
    public List<Furniture_Model> ReadOrder(String strPhone) {

        List<Furniture_Model> list = new ArrayList<>();
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM ORDER_ITEM_TABLE WHERE PHONE_NUMBER = '" + strPhone + "' ", null);

        if (cursor.moveToFirst()) {

            if (cursor.moveToFirst()) {
                do {

                    Furniture_Model furniture_model = new Furniture_Model();
                    String id = (cursor.getString(cursor.getColumnIndex("ID")));
                    furniture_model.setId(id);
                    String name = (cursor.getString(cursor.getColumnIndex("ITEM_NAME")));
                    furniture_model.setItem_name(name);
                    String price = (cursor.getString(cursor.getColumnIndex("ITEM_PRICE")));
                    furniture_model.setItem_price(price);
                    String str_image = (cursor.getString(cursor.getColumnIndex("ITEM_IMAGE")));


                    int oderPrice = Integer.parseInt(price);

                    Utilities.putValueInEditor(context).putInt("price", oderPrice).commit();


                    if (str_image.equals("null_image")) {
                        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/ic_close");
                        furniture_model.setItem_image(String.valueOf(uri));
                    } else {
                        furniture_model.setItem_image(str_image);

                    }
                    list.add(furniture_model);

                } while (cursor.moveToNext());
            }


        } else {
            Toast.makeText(context, "no exist", Toast.LENGTH_SHORT).show();
        }

        ///sum adding colum

        Cursor cursors = sqLiteDatabase.rawQuery(
                "SELECT SUM(ITEM_PRICE) FROM ORDER_ITEM_TABLE WHERE PHONE_NUMBER = '" + strPhone + "' ", null);
        if (cursors.moveToFirst()) {

            Furniture_Model furniture_model = new Furniture_Model();
            furniture_model.setTotal_price(String.valueOf(cursors.getInt(0)));

            Utilities.putValueInEditor(context).putString("total_price", String.valueOf(cursors.getInt(0))).commit();


        }
        return list;
    }

    public List<Furniture_Model> ReadAllFurniture() {

        List<Furniture_Model> list = new ArrayList<>();
        String query = "SELECT  * FROM ORDER_NAME_TABLE";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {


                Furniture_Model furniture_model = new Furniture_Model();
                String name = (cursor.getString(cursor.getColumnIndex("NAME")));
                String number = (cursor.getString(cursor.getColumnIndex("PHONE_NUMBER")));
                String str_order_date = (cursor.getString(cursor.getColumnIndex("ORDER_DATE")));
                String str_order_place_date = (cursor.getString(cursor.getColumnIndex("ORDER_PLACE_DATE")));
                String str_image = (cursor.getString(cursor.getColumnIndex("IMAGE_URI")));


                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                String dateOfDay = simpleDateFormat.format(date);
                String timeofday = android.text.format.DateFormat.format("HH:mm:ss", new Date().getTime()).toString();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
                try {
                    Date date1 = dateFormat.parse(dateOfDay + " " + "10:12:56");
                    Date date2 = dateFormat.parse(str_order_date + " " + timeofday);

                    long differenceBetweenDateLong = Utilities.printDifference(date1, date2);



                    int alarmDay =  Utilities.getSharedPreferences(context).getInt("alarm_day",0);
                    if (differenceBetweenDateLong >alarmDay) {

                        furniture_model.setItem_name(name);
                        furniture_model.setPhone_number(number);
                        furniture_model.setOrder_date(str_order_date);
                        furniture_model.setPlace_date(str_order_place_date);

                        furniture_model.setAlarm_blink_day((int) differenceBetweenDateLong);


                        if (str_image.equals("null_image")) {
                            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/ic_close");
                            furniture_model.setItem_image(String.valueOf(uri));
                        } else {
                            furniture_model.setItem_image(str_image);

                        }

                        list.add(furniture_model);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public List<Furniture_Model> ReadByDate(View view, boolean isNotificationSet) {

        List<Furniture_Model> list = new ArrayList<>();
        String query = "SELECT  * FROM ORDER_NAME_TABLE";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {


                Furniture_Model furniture_model = new Furniture_Model();
                String name = (cursor.getString(cursor.getColumnIndex("NAME")));
                String number = (cursor.getString(cursor.getColumnIndex("PHONE_NUMBER")));
                String str_order_date = (cursor.getString(cursor.getColumnIndex("ORDER_DATE")));
                String str_order_place_date = (cursor.getString(cursor.getColumnIndex("ORDER_PLACE_DATE")));
                String str_image = (cursor.getString(cursor.getColumnIndex("IMAGE_URI")));

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                String dateOfDay = simpleDateFormat.format(date);


                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                try {
                    Date date1 = dateFormat.parse(dateOfDay );
                    Date date2 = dateFormat.parse(str_order_date);

                    long differenceBetweenDateLong = Utilities.printDifference(date1, date2);


                    int alarmDay =  Utilities.getSharedPreferences(context).getInt("alarm_day",0);
                    if (differenceBetweenDateLong <= alarmDay) {
                        furniture_model.setItem_name(name);
                        furniture_model.setPhone_number(number);
                        furniture_model.setOrder_date(str_order_date);
                        furniture_model.setPlace_date(str_order_place_date);

                        furniture_model.setAlarm_blink_day((int) differenceBetweenDateLong);

                        String strName = furniture_model.getItem_name();
                        String strPhone = furniture_model.getPhone_number();
                        String strImage = furniture_model.getItem_image();
                        i++;


                        if (isNotificationSet) {
                            Utilities.createNotification(context, strName, strPhone, strImage, i);
                        }
                        if (str_image.equals("null_image")) {
                            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/ic_close");
                            furniture_model.setItem_image(String.valueOf(uri));
                        } else {
                            furniture_model.setItem_image(str_image);

                        }

                        list.add(furniture_model);
                    }

//                    // Reload current fragment
//                    Fragment frg = null;
//                    frg = ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
//                    final FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
//                    ft.detach(frg);
//                    ft.attach(frg);
//                    ft.commit();


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public void CheckAllBalence(Context context){
        Cursor cursors = sqLiteDatabase.rawQuery(
                "SELECT SUM(ITEM_PRICE) FROM ORDER_ITEM_TABLE ", null);
        if (cursors.moveToFirst()) {

            Furniture_Model furniture_model = new Furniture_Model();
            furniture_model.setTotal_price(String.valueOf(cursors.getInt(0)));

            Utilities.putValueInEditor(context).putString("total_balance", String.valueOf(cursors.getInt(0))).commit();


        }
    }

}
