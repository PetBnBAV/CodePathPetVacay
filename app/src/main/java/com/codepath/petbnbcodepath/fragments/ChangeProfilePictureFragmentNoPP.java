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


public class ChangeProfilePictureFragmentNoPP extends DialogFragment implements FragmentCameraCommunicator {


    TextView tvTakePhotoNoPP;
    TextView tvChooseFromLibraryNoPP;



    public FragmentCameraCommunicator camera_communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        camera_communicator = (FragmentCameraCommunicator) activity;
    }

    public ChangeProfilePictureFragmentNoPP() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("SET A PROFILE PICTURE");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));


        View view = inflater.inflate(R.layout.fragment_change_profile_picture_no_pp, null);


        initializeViews(view);
        setUpListerners();
        return view;

    }


    public void initializeViews(View view) {

        tvTakePhotoNoPP = (TextView) view.findViewById(R.id.tvTakePhotoNoPP);
        tvChooseFromLibraryNoPP = (TextView) view.findViewById(R.id.tvChooseFromLibraryNoPP);

    }


    public void setUpListerners() {


        tvTakePhotoNoPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_communicator.onTakePhoto();
                dismiss();
            }
        });


        tvChooseFromLibraryNoPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_communicator.onChooseFromLibrary();
                dismiss();
            }
        });
    }

    public static ChangeProfilePictureFragmentNoPP newInstance() {
        ChangeProfilePictureFragmentNoPP frag = new ChangeProfilePictureFragmentNoPP();
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
