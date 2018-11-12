package com.example.atta.furnitureapplication.view.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ajithvgiri.canvaslibrary.CanvasView;
import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CanvasFragment extends Fragment {

    private View parentView;
    RelativeLayout relativeLayout;
    CanvasView canvasView;

    public CanvasFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.btn_save_canvas)
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        parentView = inflater.inflate(R.layout.fragment_canvas, container, false);

        ButterKnife.bind(this, parentView);

        relativeLayout = parentView.findViewById(R.id.parentView);
        canvasView = new CanvasView(getActivity());
        relativeLayout.addView(canvasView);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativeLayout.setDrawingCacheEnabled(true);
                saveCanvas();


            }
        });

        return parentView;
    }


    private void saveCanvas() {

        Bitmap bitmap = relativeLayout.getDrawingCache();

        File rootFile = new File(Environment.getExternalStorageDirectory(), "/.Naveed Furniture House order Images");
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }
        String fileName = "Canvas" + System.currentTimeMillis() + ".jpg";
        File saveFile = new File(rootFile, fileName);
        FileOutputStream FOS = null;
        try {
            FOS = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FOS);
            FOS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Utilities.putValueInEditor(getActivity()).putString("canvas_image", String.valueOf(saveFile)).commit();
        Utilities.putValueInEditor(getActivity()).putBoolean("canvas", true).commit();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        Utilities.withOutBackStackConnectFragment(getActivity(), new AddItemsFragment());
    }
}
