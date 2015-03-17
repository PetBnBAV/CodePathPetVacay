package com.codepath.petbnbcodepath.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.petbnbcodepath.R;

public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        //((ImageView) rootView.findViewById(R.id.image)).setImageResource(R.drawable.ic_history);
        return rootView;
    }
}
