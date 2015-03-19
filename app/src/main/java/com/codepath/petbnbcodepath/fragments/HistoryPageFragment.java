package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.petbnbcodepath.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;

/**
 * Created by anuscorps23 on 3/12/15.
 */

public class HistoryPageFragment extends Fragment implements FragmentCommunicator{

    public static final String ARG_PAGE = "ARG_PAGE";

    TextView tvLogInBookings;
    Button btnStartExploring;

    private int mHistoryPage;

    public FragmentCommunicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (FragmentCommunicator) activity;
    }

    public static HistoryPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HistoryPageFragment fragment = new HistoryPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_page, container, false);

        tvLogInBookings = (TextView) view.findViewById(R.id.tvLogInBookings);
        btnStartExploring = (Button) view.findViewById(R.id.btnStartExploring);
        setUpClickListeners();

        return view;
    }



    public void setUpClickListeners()
    {
        tvLogInBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.login();
            }
        });

        btnStartExploring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Start Exploring", Toast.LENGTH_SHORT).show();
                communicator.startExploring();

            }
        });
    }

    @Override
    public void startExploring() {

    }

    @Override
    public void login() {

    }

    @Override
    public void listYourSpace()
    {

    }

}

