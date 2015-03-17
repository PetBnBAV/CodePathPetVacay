package com.codepath.petbnbcodepath.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;

/**
 * Created by anuscorps23 on 3/12/15.
 */
public class LandingPageFragment extends Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mLandingPage;

    public static LandingPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        LandingPageFragment fragment = new LandingPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLandingPage = getArguments().getInt(ARG_PAGE);


    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing_page, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvLandingPageTitle);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Landing page fragment here", Toast.LENGTH_SHORT).show();
            }
        });
        //tvTitle.setText("Fragment #" + mLandingPage);
        return view;
    }
}

