package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Constants;

/**
 * Created by gangwal on 3/14/15.
 */
public class ToolbarWithButtonFragment extends Fragment {

    private static Activity sActivity;
    private ToolbarListener mCallback;
    private ImageButton btnBackBlack;
    private TextView tvToolbarTitle;
    private ImageButton btSecondaryAction;

    public static ToolbarWithButtonFragment getInstance(Activity activity){
        ToolbarWithButtonFragment frag = new ToolbarWithButtonFragment();
        sActivity = activity;
        return frag;
    }

    public interface ToolbarListener {
        public void onPrevious(View view);
        public void onSecondaryButtonClicked(View view);
    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ToolbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ToolbarListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_toolbar_with_button,parent,false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final String toolbarTitle = bundle.getString(Constants.titleKey);
            final int secondaryIcon = bundle.getInt(Constants.SECONADRY_ICON);
            btnBackBlack  = (ImageButton) view.findViewById(R.id.btnBackBlack);
            tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
            btSecondaryAction = (ImageButton) view.findViewById(R.id.btSecondaryAction);
            tvToolbarTitle.setText(toolbarTitle);
            btSecondaryAction.setImageResource(secondaryIcon);
            setupViewListeners();
        }
        return view;
    }

    public void setupViewListeners() {
        btnBackBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPrevious(btnBackBlack);
            }
        });
        btSecondaryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSecondaryButtonClicked(btSecondaryAction);
            }
        });
    }


}
