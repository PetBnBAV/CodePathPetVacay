package com.codepath.petbnbcodepath.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;

/**
 * Created by anuscorps23 on 3/12/15.
 */
public class HistoryPageUserFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    TextView tvLogInBookings;
    Button btnStartExploring;

    private int mHistoryPageUser;


    public static HistoryPageUserFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HistoryPageUserFragment fragment = new HistoryPageUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryPageUser = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_user_page, container, false);

        tvLogInBookings = (TextView) view.findViewById(R.id.tvLogInBookings);
        btnStartExploring = (Button) view.findViewById(R.id.btnStartExploring);

        return view;
    }

}

