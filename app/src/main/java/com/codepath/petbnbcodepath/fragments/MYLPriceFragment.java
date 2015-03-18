package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;

/**
 * Created by gangwal on 3/14/15.
 */
public class MYLPriceFragment extends Fragment {

    private PriceListingListner mCallback;
    private EditText etValue;
    private TextView tvSuggestedPrice;
    private ImageView ivHint;
    private static int  sPrice;
    private static int suggestedPrice =92;
    public static MYLPriceFragment getInstance(Activity activity, int price){
        MYLPriceFragment frag = new MYLPriceFragment();
        sPrice = price;
        return frag;
    }

    public interface PriceListingListner {
        public void postListing(String value);
    }


    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (PriceListingListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PriceListingListner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_myl_title,parent,false);
        setUpViews(view);
        setupListners(view);
        return view;

    }

    private void setupListners(View view) {
        //TODO Once Done button is there,make this as dialog
        ivHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mCallback.postListing((String.valueOf(etValue.getText())));
            }
        });
    }

    private void setUpViews(View view) {
        etValue = (EditText)view.findViewById(R.id.etValue);
        tvSuggestedPrice = (TextView)view.findViewById(R.id.tvWordCount);
        tvSuggestedPrice.setText(getString(R.string.suggestedPrice,suggestedPrice));
        ivHint = (ImageView)view.findViewById(R.id.ivHint);
        etValue.setHint(R.string.myl_titleHint);
        etValue.setText(String.valueOf(sPrice));
    }
}
