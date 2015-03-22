package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Utils;

/**
 * Created by gangwal on 3/14/15.
 */
public class MYLPriceFragment extends Fragment {

    private PriceListingListener mCallback;
    private EditText etValue;
    private TextView tvSuggestedPrice;
    private ImageView ivHint;
    private static int  sPrice;
    private static int suggestedPrice =35;
    private TextView tvDone;

    public static MYLPriceFragment getInstance(Activity activity, int price){
        MYLPriceFragment frag = new MYLPriceFragment();
        sPrice = price;
        return frag;
    }

    public interface PriceListingListener {
        public void postListing(String value);
        public void setToolbarForFragment();
    }


    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (PriceListingListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PriceListingListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_myl_price,parent,false);
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
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.postListing((String.valueOf(etValue.getText())));
                Utils.hideKeyboard(getActivity());
            }
        });
    }

    private void setUpViews(View view) {
        mCallback.setToolbarForFragment();
        etValue = (EditText)view.findViewById(R.id.etValue);
        tvSuggestedPrice = (TextView)view.findViewById(R.id.tvSuggestedPrice);
        tvSuggestedPrice.setText(getString(R.string.suggestedPrice,suggestedPrice));
        ivHint = (ImageView)view.findViewById(R.id.ivHint);
        etValue.setHint(R.string.currencyUnit);
//        etValue.setText(String.valueOf(sPrice));
        tvDone = (TextView) getActivity().findViewById(R.id.tvToolbarTitle);
    }
}
