package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;
import com.parse.ParseUser;


public class WhyHostFragment extends Fragment implements FragmentCommunicator{

    // Store instance variables

    private int pageNumber;
    private TextView tvWHTitle;
    private TextView tvWHText;
    private ImageView ivWHImage;
    private Button btnWHListYourSpace;
    private Button btnWHBack;

    public FragmentCommunicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (FragmentCommunicator) activity;
    }

    // newInstance constructor for creating fragment with arguments
    public static WhyHostFragment newInstance(int page) {
        WhyHostFragment fragmentHowItWorks = new WhyHostFragment();
        Bundle args = new Bundle();
        args.putInt("pageNumber", page);
        fragmentHowItWorks.setArguments(args);
        return fragmentHowItWorks;
    }



    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("pageNumber");
    }



    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whyhost, container, false);

        tvWHTitle = (TextView) view.findViewById(R.id.tvWHTitle);
        tvWHText = (TextView) view.findViewById(R.id.tvWHText);
        ivWHImage = (ImageView) view.findViewById(R.id.ivWHImage);
        btnWHListYourSpace = (Button) view.findViewById(R.id.btnWHList);
        btnWHBack = (Button) view.findViewById(R.id.btnWHBack);

        setupClickListeners();

        setUpPage(pageNumber);

        return view;
    }



    public void fitImageToRetainAspectRatio(int page)
    {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int targetWidth = size.x;
        Drawable headerDrawable = null;
        Bitmap bMap = null;

        switch(page)
        {
            case 0:
                headerDrawable = getResources().getDrawable(R.drawable.pet2);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet2);
                break;

            case 1:
                headerDrawable = getResources().getDrawable(R.drawable.pet4);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet4);
                break;

            case 2:
                headerDrawable = getResources().getDrawable(R.drawable.pet6);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet6);
                break;
        }

        if(headerDrawable != null){
            int currWidth = headerDrawable.getIntrinsicWidth();
            int currHeight = headerDrawable.getIntrinsicHeight();
            int origAspectRatio = currWidth / currHeight;
            int targetHeight = targetWidth / origAspectRatio;

            // Resize the bitmap to 150x100 (width x height)
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, targetWidth, targetHeight, true);
            // Loads the resized Bitmap into an ImageView
            ivWHImage.setImageBitmap(bMapScaled);

        }
    }



    public void setupClickListeners()
    {
        btnWHBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });


        btnWHListYourSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();

            }
        });

        btnWHListYourSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    communicator.listYourSpace();

                } else {
                    communicator.login();
                }
            }
        });
    }



    public void setUpPage(int pageNumber)
    {
        switch(pageNumber)
        {
            case 0:
                tvWHTitle.setText(getResources().getString(R.string.WHTitleOne));
                tvWHText.setText(getResources().getString(R.string.WHTextOne));
                fitImageToRetainAspectRatio(0);
                btnWHListYourSpace.setBackgroundColor(getResources().getColor(R.color.petvacayteal));
                break;

            case 1:
                tvWHTitle.setText(getResources().getString(R.string.WHTitleTwo));
                tvWHText.setText(getResources().getString(R.string.WHTextTwo));
                fitImageToRetainAspectRatio(1);
                btnWHListYourSpace.setBackgroundColor(getResources().getColor(R.color.darkpurple));
                break;

            case 2:
                tvWHTitle.setText(getResources().getString(R.string.WHTitleThree));
                tvWHText.setText(getResources().getString(R.string.WHTextThree));
                fitImageToRetainAspectRatio(2);
                btnWHListYourSpace.setBackgroundColor(getResources().getColor(R.color.red));
                break;
        }

    }

    @Override
    public void startExploring() {

    }

    @Override
    public void login() {


    }
    @Override
    public void listYourSpace() {


    }
}