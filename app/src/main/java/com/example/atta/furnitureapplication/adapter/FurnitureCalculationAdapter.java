package com.example.atta.furnitureapplication.adapter;

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
import android.widget.Toast;

import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;
import com.example.atta.furnitureapplication.view.activity.Main2Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FurnitureCalculationAdapter extends RecyclerView.Adapter<FurnitureCalculationAdapter.ViewHolder> {

    private List<Furniture_Model> furniture_models;
    private Context context;
    private Furniture_CURD furniture_curd;


    public FurnitureCalculationAdapter(List<Furniture_Model> contact_models, Context context, Furniture_CURD furniture_curd) {
        this.furniture_models = contact_models;
        this.context = context;
        this.furniture_curd = furniture_curd;

    }


    @NonNull
    @Override
    public FurnitureCalculationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calculation_layout, viewGroup, false);

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final FurnitureCalculationAdapter.ViewHolder viewHolder, int i) {


        final Furniture_Model furniture_model = furniture_models.get(i);

        viewHolder.tvName.setText(furniture_model.getStrPaymentName());
        viewHolder.tvDate.setText(FormatedDate(furniture_model.getStrDate()));
        viewHolder.tvPayment.setText(furniture_model.getStrAdvance());

        viewHolder.tvPayment.setOnLongClickListener(new View.OnLongClickListener() {
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
        private TextView tvName, tvPayment, tvDate;
        private LinearLayout llFurniture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvName = itemView.findViewById(R.id.tv_payment_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPayment = itemView.findViewById(R.id.tv_payment);

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
        spf= new SimpleDateFormat("dd/MM/yyyy");
        strDate = spf.format(newDate);

        return strDate;
    }


}
