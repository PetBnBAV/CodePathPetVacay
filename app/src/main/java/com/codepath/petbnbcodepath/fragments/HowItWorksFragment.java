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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;
import com.parse.ParseUser;

public class HowItWorksFragment extends Fragment implements FragmentCommunicator {

    // Store instance variables

    private int pageNumber;
    private TextView tvHSWTitle;
    private TextView tvHSWText;
    private ImageView ivHSWImage;
    private Button btnHSWLogIn;
    private Button btnHSWSignUp;
    private Button btnHSWBack;
    private Button btnHSWSkip;
    private LinearLayout linearLayoutHSW;

    private int[] mImages;

    public FragmentCommunicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (FragmentCommunicator) activity;
    }



    // newInstance constructor for creating fragment with arguments
    public static HowItWorksFragment newInstance(int page) {
        HowItWorksFragment fragmentHowItWorks = new HowItWorksFragment();
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
        View view = inflater.inflate(R.layout.fragment_howitworks, container, false);

        tvHSWTitle = (TextView) view.findViewById(R.id.tvHSWTitle);
        tvHSWText = (TextView) view.findViewById(R.id.tvHSWText);
        ivHSWImage = (ImageView) view.findViewById(R.id.ivHSWImage);
        btnHSWLogIn = (Button) view.findViewById(R.id.btnHSWLogIn);
        btnHSWSignUp = (Button) view.findViewById(R.id.btnHSWSignUp);
        btnHSWBack = (Button) view.findViewById(R.id.btnHSWBack);
        btnHSWSkip = (Button) view.findViewById(R.id.btnHSWSkip);
        btnHSWSignUp = (Button) view.findViewById(R.id.btnHSWSignUp);
        btnHSWLogIn = (Button) view.findViewById(R.id.btnHSWLogIn);

        linearLayoutHSW = (LinearLayout) view.findViewById(R.id.linearLayoutHSW);
        setupClickListeners();


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            linearLayoutHSW.setVisibility(View.INVISIBLE);

        }

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
                headerDrawable = getResources().getDrawable(R.drawable.pet1);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet1);
                break;

            case 1:
                headerDrawable = getResources().getDrawable(R.drawable.pet3);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet3);
                break;

            case 2:
                headerDrawable = getResources().getDrawable(R.drawable.pet5);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet5);
                break;


            case 3:
                headerDrawable = getResources().getDrawable(R.drawable.pet7);
                bMap = BitmapFactory.decodeResource(getResources(), R.drawable.pet7);
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
        ivHSWImage.setImageBitmap(bMapScaled);

        }
    }



    public void setupClickListeners()
    {
        btnHSWBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });


        btnHSWSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });

        btnHSWLogIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            communicator.login();
        }
        });

        btnHSWSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.login();

            }
        });


    }



    public void setUpPage(int pageNumber)
    {
        switch(pageNumber)
        {
            case 0:
                tvHSWTitle.setText(getResources().getString(R.string.HSWTitleOne));
                tvHSWText.setText(getResources().getString(R.string.HSWTextOne));
                fitImageToRetainAspectRatio(0);
                btnHSWLogIn.setBackgroundColor(getResources().getColor(R.color.petvacayteal));
                btnHSWSignUp.setBackgroundColor(getResources().getColor(R.color.petvacayteal));
                break;

            case 1:
                tvHSWTitle.setText(getResources().getString(R.string.HSWTitleTwo));
                tvHSWText.setText(getResources().getString(R.string.HSWTextTwo));
                fitImageToRetainAspectRatio(1);
                btnHSWLogIn.setBackgroundColor(getResources().getColor(R.color.darkpurple));
                btnHSWSignUp.setBackgroundColor(getResources().getColor(R.color.darkpurple));
                break;

            case 2:
                tvHSWTitle.setText(getResources().getString(R.string.HSWTitleThree));
                tvHSWText.setText(getResources().getString(R.string.HSWTextThree));
                fitImageToRetainAspectRatio(2);
                btnHSWLogIn.setBackgroundColor(getResources().getColor(R.color.red));
                btnHSWSignUp.setBackgroundColor(getResources().getColor(R.color.red));
                break;

            case 3:
                tvHSWTitle.setText(getResources().getString(R.string.HSWTitleFour));
                tvHSWText.setText(getResources().getString(R.string.HSWTextFour));
                fitImageToRetainAspectRatio(3);
                btnHSWLogIn.setBackgroundColor(getResources().getColor(R.color.darkblue));
                btnHSWSignUp.setBackgroundColor(getResources().getColor(R.color.darkblue));
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



