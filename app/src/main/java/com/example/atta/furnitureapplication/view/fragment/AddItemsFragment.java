package com.example.atta.furnitureapplication.view.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;
import com.example.atta.furnitureapplication.adapter.Furniture_Model;
import com.example.atta.furnitureapplication.adapter.OrderGridAdapter;
import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import ng.max.slideview.SlideView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFragment extends Fragment {

    private View parentView, dialogView;
    private List<Furniture_Model> furniture_models;
    private Furniture_CURD furniture_curd;

    private int REQUEST_PHONE_CALL;

    @BindView(R.id.iv_add_item)
    ImageView ivAddItem;

    @BindView(R.id.tv_item_name)
    TextView tvItemName;

    @BindView(R.id.iv_add_item_call)
    ImageView ivCall;

    @BindView(R.id.gridView)
    GridView gridView;
    private LayoutInflater inflater;

    private Dialog addItemDialog, pickUpImageDialog;
    private Uri image_uri, cameraImageUri;
    private File file;


    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;

    @BindView(R.id.slideView)
    SlideView slideView;

    private ImageView ivItemImage;

    private String strCanvasImage;
    private boolean aBooleanCanvas;

    public AddItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_add_items, container, false);

        this.inflater = inflater;
        ButterKnife.bind(this, parentView);
        furniture_curd = new Furniture_CURD(getActivity());

        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {


                Utilities.connectFragment(getActivity(),new CalculationFragment());

            }
        });


        aBooleanCanvas = Utilities.getSharedPreferences(getActivity()).getBoolean("canvas", false);

        if (aBooleanCanvas) {

            addItemDialog();

        }

        tvItemName.setText(Utilities.getSharedPreferences(getActivity()).getString("order_item_name", ""));
//


        String strPhone = Utilities.getSharedPreferences(getActivity()).getString("order_item_phone", "");
        furniture_models = furniture_curd.ReadOrder(strPhone);

        gridView.setAdapter(new OrderGridAdapter(getActivity(), R.layout.custom_order_item_layout, furniture_models));


        slideView.setText("Total Amount : "+Utilities.getSharedPreferences(getActivity()).getString("total_price", "")+"             ");


        ivAddItem = parentView.findViewById(R.id.iv_add_item);
        ivAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog();
            }
        });


        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strItemPhone = Utilities.getSharedPreferences(getActivity()).getString("order_item_phone", "");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strItemPhone));

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(intent);
                }

            }
        });

        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //  Log.i(tag, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //   Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });

        return parentView;
    }


    private void addItemDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogView = inflater.inflate(R.layout.add_item_layout, null);
        dialogView.setBackgroundResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        addItemDialog = dialogBuilder.create();
        ivItemImage = dialogView.findViewById(R.id.iv_item_image);
        final EditText etItemName = dialogView.findViewById(R.id.et_item_name);
        final EditText etItemPrice = dialogView.findViewById(R.id.et_item_price);
        final Button btnDone = dialogView.findViewById(R.id.btn_item_done);


        if (aBooleanCanvas) {
            String strCanvas = Utilities.getSharedPreferences(getActivity()).getString("canvas_image", "");
            image_uri = Uri.parse(strCanvas);
            ivItemImage.setImageURI(Uri.parse(strCanvas));
            Utilities.putValueInEditor(getActivity()).putBoolean("canvas", false).commit();
            aBooleanCanvas = false;
        }

        ivItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpImagedialog();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strItemName = etItemName.getText().toString();
                String strItemPrice = etItemPrice.getText().toString();
                if (image_uri == null) {
                    Toast.makeText(getActivity(), "set image", Toast.LENGTH_SHORT).show();

                } else if (strItemName.length() <= 1) {
                    etItemName.setError("enter item name");
                } else if (strItemPrice.length() <= 0) {
                    etItemPrice.setError("enter item price");
                } else {
                    String strItemPhone = Utilities.getSharedPreferences(getActivity()).getString("order_item_phone", "");


                    if (furniture_curd.checkInsertItem(strItemName, strItemPhone)) {
                        furniture_curd.insertItemsExist(String.valueOf(image_uri), strItemName, strItemPrice, strItemPhone);
                        addItemDialog.dismiss();

                    }
                }
            }
        });

        addItemDialog.show();
        addItemDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_uri = resultUri;
                final ImageView ivItemImage = dialogView.findViewById(R.id.iv_item_image);
                ivItemImage.setImageURI(image_uri);
                pickUpImageDialog.dismiss();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


        switch (requestCode) {
            case 2://capture image


                if (resultCode == RESULT_OK) {


                    File rootFile = new File(Environment.getExternalStorageDirectory(), "/.Naveed Furniture House order Images");
                    if (!rootFile.exists()) {
                        rootFile.mkdir();
                    }
                    try {

                        File compressedImageFile = new Compressor(getActivity())
                                .setMaxWidth(1080)
                                .setMaxHeight(840)
                                .setQuality(100)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setDestinationDirectoryPath(rootFile.getAbsolutePath())
                                .compressToFile(file);
                        image_uri = Uri.fromFile(compressedImageFile);

                        File filedelte = new File(cameraImageUri.getPath());
                        filedelte.delete();
                        if (filedelte.exists()) {
                            filedelte.getCanonicalFile().delete();
                            if (filedelte.exists()) {
                                getActivity().getApplicationContext().deleteFile(filedelte.getName());
                            }
                        }


                        if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                        } else {

                            startCropImageActivity(image_uri);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();


                }
                break;
            case 1://gallary
                if (resultCode == RESULT_OK) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                            Date());


                    File rootFile = new File(Environment.getExternalStorageDirectory(), "/.Naveed Furniture House order Images");
                    if (!rootFile.exists()) {
                        rootFile.mkdir();
                    }

                    image_uri = data.getData();
                    String string = getImagePath(image_uri);
                    File file = new File(string);

                    long image_size = file.length() / 1024;
                    if (image_size == 0) {
                        Toast.makeText(getActivity(), "Image Corrupted", Toast.LENGTH_SHORT).show();

                    } else {
                        try {
                            File compressedImageFile = new Compressor(getActivity())
                                    .setMaxWidth(1080)
                                    .setMaxHeight(840)
                                    .setQuality(100)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .setDestinationDirectoryPath(rootFile.getAbsolutePath())
                                    .compressToFile(file);
                            image_uri = Uri.fromFile(compressedImageFile);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                        } else {

                            startCropImageActivity(image_uri);
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private void pickUpImagedialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final View dialogView = inflater.inflate(R.layout.pick_up_images_layout, null);
        dialogView.setBackgroundResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        pickUpImageDialog = dialogBuilder.create();
        final TextView tvGallery = dialogView.findViewById(R.id.tv_gallery);
        final TextView tvCamera = dialogView.findViewById(R.id.tv_camera);
        final LinearLayout llCanvas = dialogView.findViewById(R.id.ll_draw_canvas);
        llCanvas.setVisibility(View.VISIBLE);
        final TextView tvNoImage = dialogView.findViewById(R.id.tv_no_image);

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGalleryPic();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCameraPic();
            }
        });

        llCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog.dismiss();
                pickUpImageDialog.dismiss();
                Utilities.connectFragment(getActivity(), new CanvasFragment());
            }
        });

        tvNoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_uri = Uri.parse("null_image");
                ivItemImage.setImageURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/ic_close"));
                pickUpImageDialog.dismiss();

            }
        });

        pickUpImageDialog.show();
        pickUpImageDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {


                return false;
            }
        });
    }


    public void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createImageFile();
//        boolean isDirectoryCreated = file.getParentFile().mkdirs();
//        Log.d("", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            cameraImageUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    "com.scanlibrary.provider", // As defined in Manifest
                    file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        } else {
            Uri tempFileUri = Uri.fromFile(file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
        }
        startActivityForResult(cameraIntent, 2);
    }

    private File createImageFile() {
        clearTempImages();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());


        file = new File(Environment
                .getExternalStorageDirectory(), timeStamp + ".jpg");

        cameraImageUri = Uri.fromFile(file);

        return file;
    }

    private void clearTempImages() {
        try {
            File tempFolder = new File(Environment
                    .getExternalStorageDirectory().getPath() + "/scanSample");
            for (File f : tempFolder.listFiles())
                f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void intentCameraPic() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        openCamera();

    }

    public void intentGalleryPic() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code


    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(getActivity(), this);
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }


}
