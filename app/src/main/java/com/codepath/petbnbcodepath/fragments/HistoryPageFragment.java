package com.codepath.petbnbcodepath.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;

/**
 * Created by anuscorps23 on 3/12/15.
 */
public class HistoryPageFragment extends Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mHistoryPage;

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
        TextView tvTitle = (TextView) view.findViewById(R.id.tvHistoryPageTitle);
        //tvTitle.setText("Fragment #" + mHistoryPage);
        return view;
    }
}

