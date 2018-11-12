package com.example.atta.furnitureapplication.view.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atta.furnitureapplication.R;
import com.example.atta.furnitureapplication.GenrelUtills.Utilities;
import com.github.chrisbanes.photoview.PhotoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemPhotoViewFragment extends Fragment {


    private View parentView;


    public ItemPhotoViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_photo_view, container, false);

        PhotoView photoView = parentView.findViewById(R.id.photo_view);
        String strImage = Utilities.getSharedPreferences(getActivity()).getString("image_uri","");
        Uri imageUri = Uri.parse(strImage);
        photoView.setImageURI(imageUri);

        return parentView;
    }

}
