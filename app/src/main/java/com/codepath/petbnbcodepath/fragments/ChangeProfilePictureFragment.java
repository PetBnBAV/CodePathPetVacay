package com.codepath.petbnbcodepath.fragments;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.interfaces.FragmentCameraCommunicator;


public class ChangeProfilePictureFragment extends DialogFragment implements FragmentCameraCommunicator {

    TextView tvRemoveCurrentPhoto;
    TextView tvTakePhoto;
    TextView tvChooseFromLibrary;


    public FragmentCameraCommunicator camera_communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        camera_communicator = (FragmentCameraCommunicator) activity;
    }

    public ChangeProfilePictureFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("SET A PROFILE PICTURE");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));


        View view = inflater.inflate(R.layout.fragment_change_profile_picture, null);


        initializeViews(view);
        setUpListerners();
        return view;

    }


    public void initializeViews(View view) {
        tvRemoveCurrentPhoto = (TextView) view.findViewById(R.id.tvRemoveCurrentPhoto);
        tvTakePhoto = (TextView) view.findViewById(R.id.tvTakePhoto);
        tvChooseFromLibrary = (TextView) view.findViewById(R.id.tvChooseFromLibrary);

    }


    public void setUpListerners() {


        tvRemoveCurrentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_communicator.onRemoveCurrentPhoto();
                dismiss();
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_communicator.onTakePhoto();
                dismiss();
            }
        });


        tvChooseFromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_communicator.onChooseFromLibrary();
                dismiss();
            }
        });
    }

    public static ChangeProfilePictureFragment newInstance() {
        ChangeProfilePictureFragment frag = new ChangeProfilePictureFragment();
        return frag;
    }


    @Override
    public void onRemoveCurrentPhoto() {

    }

    @Override
    public void onTakePhoto() {

    }

    @Override
    public void onChooseFromLibrary() {

    }
}
