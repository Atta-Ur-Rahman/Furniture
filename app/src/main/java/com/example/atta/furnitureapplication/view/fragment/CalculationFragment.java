package com.example.atta.furnitureapplication.view.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.adapter.FurnitureAdapter;
import com.example.atta.furnitureapplication.adapter.FurnitureCalculationAdapter;
import com.example.atta.furnitureapplication.adapter.Furniture_Model;
import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculationFragment extends Fragment {

    private View parentView;
    private String strTotalAmount, strPhoneNumber, strHolderName, strDate, strAdavanceName, strAdvance, strTotalAdvance;

    @BindView(R.id.tv_item_holder_name)
    TextView tvItemHolderName;
    @BindView(R.id.et_payment_name)
    EditText etPaymentName;
    @BindView(R.id.et_payment)
    EditText etPayment;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_total_advance)
    TextView tvTotalAdvance;
    @BindView(R.id.tv_total_arrears)
    TextView tvTotalArrears;

    @BindView(R.id.iv_add_item_call)ImageView ivCall;


    @BindView(R.id.btn_advance_save)
    Button btnAdvanceSave;

    @BindView(R.id.rv_advance)
    RecyclerView rvAdvance;

    private List<Furniture_Model> furniture_models;
    private Furniture_CURD furniture_curd;


    public CalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, parentView);
        furniture_curd = new Furniture_CURD(getActivity());


        strTotalAmount = Utilities.getSharedPreferences(getActivity()).getString("total_price", "");
        strHolderName = Utilities.getSharedPreferences(getActivity()).getString("order_item_name", "");
        strPhoneNumber = Utilities.getSharedPreferences(getActivity()).getString("order_item_phone", "");

        tvItemHolderName.setText(strHolderName);
        etPaymentName.setText(strHolderName);
        tvTotalAmount.setText(strTotalAmount);


        rvAdvance.setHasFixedSize(true);
        rvAdvance.setLayoutManager(new LinearLayoutManager(getActivity()));

        furniture_models = furniture_curd.ReadPayment(getActivity(),strPhoneNumber);
        FurnitureCalculationAdapter adapter = new FurnitureCalculationAdapter(furniture_models, getActivity(), furniture_curd);
        rvAdvance.setAdapter(adapter);


        btnAdvanceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
                strDate = (df.format(c));
                strAdavanceName = etPaymentName.getText().toString();
                strAdvance = etPayment.getText().toString();

                if (strAdavanceName.equals("")) {
                    etPaymentName.setError("enter name");
                } else if (strAdvance.equals("")) {
                    etPayment.setError("enter payment");
                } else {

                    String name  = strAdavanceName;
                    name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                    furniture_curd.insertPayment(name, strAdvance, strPhoneNumber, strDate);
                }


            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                strTotalAdvance = Utilities.getSharedPreferences(getActivity()).getString("total_advance", "");
                tvTotalAdvance.setText(strTotalAdvance);


                if (strTotalAdvance.equals("") || strTotalAmount.equals("")) {
                    tvTotalAdvance.setText("No Advance");
                }else {
                    double totalAmount = Double.parseDouble(strTotalAmount);
                    double totalAdvance = Double.parseDouble(strTotalAdvance);
                    double totalArrears = totalAmount - totalAdvance;

                    DecimalFormat format = new DecimalFormat("#00,00,000.00");
                    String formattedNumber = format.format(totalArrears);

                    tvTotalArrears.setText(String.valueOf(formattedNumber));

                }


            }
        }, 500);




        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //  Log.i(tag, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //   Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().getSupportFragmentManager().popBackStack();

                    Utilities.withOutBackStackConnectFragmentWithOutAnimination(getActivity(),new AddItemsFragment());

                    return true;
                }
                return false;
            }
        });



        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strPhoneNumber));

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    int REQUEST_PHONE_CALL = 0;
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(intent);
                }

            }
        });

        return parentView;
    }


}
