package com.example.atta.furnitureapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.ViewHolder> {

    private List<Furniture_Model> furniture_models;
    private Context context;
    private Furniture_CURD furniture_curd;


    public FurnitureAdapter(List<Furniture_Model> contact_models, Context context, Furniture_CURD furniture_curd) {
        this.furniture_models = contact_models;
        this.context = context;
        this.furniture_curd = furniture_curd;

    }


    @NonNull
    @Override
    public FurnitureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.furniture_layout, viewGroup, false);

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final FurnitureAdapter.ViewHolder viewHolder, int i) {


        final Furniture_Model furniture_model = furniture_models.get(i);

        viewHolder.tvItemName.setText(furniture_model.getItem_name());
        viewHolder.tvOrderPhone.setText(furniture_model.getPhone_number());
        viewHolder.ivItemImage.setImageURI(Uri.parse(furniture_model.getItem_image()));
        viewHolder.tvOderDate.setText(FormatedDate(furniture_model.getOrder_date()));
        viewHolder.tvOrderPlaceDate.setText(FormatedDate(furniture_model.getPlace_date()));


        int diff_day = furniture_model.getAlarm_blink_day();
        int alarmDay = Utilities.getSharedPreferences(context).getInt("alarm_day", 0);
        viewHolder.tvAlarmDay.setText(String.valueOf(diff_day) + " days");
        if (diff_day <= alarmDay) {

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink_anim);
            viewHolder.ivAlarm.setAnimation(animation);
            viewHolder.ivAlarm.setVisibility(View.VISIBLE);
            viewHolder.tvOderDate.setTextColor(context.getResources().getColor(R.color.red));

        }
        viewHolder.llFurniture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Main2Activity.class));
                Utilities.putValueInEditor(context).putString("order_item_phone", null).commit();
                Utilities.putValueInEditor(context).putString("order_item_phone", furniture_model.getPhone_number()).commit();
                Utilities.putValueInEditor(context).putString("order_item_name", "").commit();
                Utilities.putValueInEditor(context).putString("order_item_name", furniture_model.getItem_name()).commit();

            }
        });

        viewHolder.llFurniture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
                pictureDialog.setTitle("Order");
                String[] pictureDialogItems = {
                        "\tEdit",
                        "\tDelete"};
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:



                                        break;
                                    case 1:

                                        AlertDialog.Builder builder;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(context);
                                        }
                                        builder.setTitle("Deleting")
                                                .setMessage("Do you want to delete this order")
                                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        String strPhone = furniture_model.getPhone_number();
                                                        furniture_curd.Delete(strPhone, context);
                                                    }
                                                })
                                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_menu_delete)
                                                .show();

                                        break;
                                }
                            }
                        });
                pictureDialog.show();

                return false;
            }
        });


    }



    @Override
    public int getItemCount() {
        return furniture_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivItemImage, ivAlarm;
        private TextView tvItemName, tvOrderPhone, tvOderDate, tvOrderPlaceDate, tvAlarmDay;
        private LinearLayout llFurniture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvOrderPhone = itemView.findViewById(R.id.tv_order_phone);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
            tvOrderPlaceDate = itemView.findViewById(R.id.tv_order_place_date);
            tvOderDate = itemView.findViewById(R.id.tv_order_date);
            llFurniture = itemView.findViewById(R.id.ll_furniture);
            ivAlarm = itemView.findViewById(R.id.iv_alarm);
            tvAlarmDay = itemView.findViewById(R.id.tv_alarm_day);


        }
    }


    private String FormatedDate(String date) {

        String strDate = null;
        SimpleDateFormat spf=new SimpleDateFormat("dd MM yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd:MM:yyyy");
        strDate = spf.format(newDate);

        return strDate;
    }


}
