package com.example.atta.furnitureapplication.view.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atta.furnitureapplication.dataBase.Furniture_CURD;
import com.example.atta.furnitureapplication.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrderFragment extends Fragment implements View.OnClickListener {

    private View parentView;

    @BindView(R.id.iv_order_image)
    CircleImageView ivOrderImage;

    @BindView(R.id.et_order_name)
    EditText etOrderName;

    @BindView(R.id.et_order_phone_number)
    EditText etOrderPhoneNumber;

    @BindView(R.id.et_view_item_order_place_time)
    TextView etOrderPlaceTime;
    @BindView(R.id.et_view_item_order_order_time)
    TextView etOrderTime;

    @BindView(R.id.btn_oder_done)
    Button btnOrderDone;

    private Uri cameraImageUri, image_uri;

    private File file;


    private String strOrderName, strOrderPhoneNumber, strOrderPlaceDate, strOrderDate, strImageUri;

    private Furniture_CURD furniture_curd;

    private Dialog pickUpImageDialog;

    private LayoutInflater inflater;


    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;


    public AddOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, parentView);

        furniture_curd = new Furniture_CURD(getContext());

        furniture_curd.CheckAllBalence(getActivity());

        this.inflater = inflater;

        initCalendar();

        ivOrderImage.setOnClickListener(this);
        etOrderPlaceTime.setOnClickListener(this);
        etOrderTime.setOnClickListener(this);
        btnOrderDone.setOnClickListener(this);

        return parentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_uri = resultUri;
                ivOrderImage.setImageURI(image_uri);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_order_image:

                pickUpImagedialog();
                break;
            case R.id.et_view_item_order_place_time:

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
                etOrderPlaceTime.setText(df.format(c));

                break;
            case R.id.et_view_item_order_order_time:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                break;
            case R.id.btn_oder_done:


                if (image_uri == null) {
                    Toast.makeText(getActivity(), "Set image", Toast.LENGTH_SHORT).show();
                } else if (etOrderName.getText().length() <= 1) {
                    etOrderName.setError("enter name");
                } else if (etOrderPhoneNumber.getText().length() <= 10) {
                    etOrderPhoneNumber.setError("enter valid phone number");
                } else if (etOrderPlaceTime.getText() == null) {
                    etOrderPlaceTime.setError("enter order place time");
                } else if (etOrderTime.getText().length() <= 1) {
                    etOrderTime.setError("enter order time");
                } else {

                    strOrderName = etOrderName.getText().toString();
                    strOrderPhoneNumber = etOrderPhoneNumber.getText().toString();
                    strOrderPlaceDate = etOrderPlaceTime.getText().toString();
                    strOrderDate = etOrderTime.getText().toString();

                    if (furniture_curd.checkOrderExist(strOrderPhoneNumber)) {
                        furniture_curd.insertOrder(strOrderName, strOrderPhoneNumber, strOrderDate, strOrderPlaceDate, String.valueOf(image_uri));
                    }
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

        tvNoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_uri = Uri.parse("null_image");
                ivOrderImage.setImageURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/drawable/ic_close"));

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

    private void initCalendar() {

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
    }

    private void updateLabel() {
        String myFormat = "dd MM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etOrderTime.setText(sdf.format(myCalendar.getTime()));
    }

}
