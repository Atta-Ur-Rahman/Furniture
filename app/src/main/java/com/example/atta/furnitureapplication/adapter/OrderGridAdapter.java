package com.example.atta.furnitureapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;
import com.example.atta.furnitureapplication.view.fragment.AddItemsFragment;
import com.example.atta.furnitureapplication.view.fragment.ItemPhotoViewFragment;

import java.util.List;

public class OrderGridAdapter extends ArrayAdapter {

    private List<Furniture_Model> furniture_models;
    private Context context;
    private int layoutResourceId;
    private Furniture_CURD furniture_curd;

    public OrderGridAdapter(Context context, int layoutResourceId, List<Furniture_Model> furniture_models) {
        super(context, layoutResourceId);
        this.furniture_models = furniture_models;
        this.context = context;
        this.layoutResourceId = layoutResourceId;

        furniture_curd = new Furniture_CURD(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;


        if (row == null) {
            LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.image = row.findViewById(R.id.iv_book_view);
            holder.tvName = row.findViewById(R.id.tv_name);
            holder.tvPrice = row.findViewById(R.id.tv_price);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Furniture_Model furniture_model = furniture_models.get(position);

        holder.tvName.setText(furniture_model.getItem_name());
        holder.tvPrice.setText(furniture_model.getItem_price());
        holder.image.setImageURI(Uri.parse(furniture_model.getItem_image()));


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Utilities.putValueInEditor(context).putString("image_uri", furniture_model.getItem_image()).commit();
                Utilities.connectFragment(context, new ItemPhotoViewFragment());


            }
        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
                pictureDialog.setTitle("Item");
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
                                                .setMessage("Do you want to delete this item")
                                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String strId = furniture_models.get(position).getId();
                                                        furniture_curd.DeleteItem(strId, context);
                                                        Utilities.withOutBackStackConnectFragment(context, new AddItemsFragment());
                                                    }
                                                })
                                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_menu_delete)
                                                .show();
                                }
                            }
                        });
                pictureDialog.show();

                return false;
            }
        });


        return row;
    }


    @Override
    public int getCount() {
        return furniture_models.size();
    }

    static class ViewHolder {
        ImageView image;
        TextView tvName, tvPrice;
    }

}

