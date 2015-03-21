package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Utils;

import java.util.StringTokenizer;

/**
 * Created by gangwal on 3/14/15.
 */
public class MYLLandingPageFragment extends Fragment {

    private PostListingListner mCallback;
    private TextView tvTitle;
    private EditText etValue;
    private TextView tvWordCount;
    private ImageView ivHint;
    private TextView tvDone;
    private static int sMaxCount,sFieldType;
    private static String  sValue;
    public static MYLLandingPageFragment getInstance(Activity activity, int maxCount,int fieldType, String value){
        MYLLandingPageFragment frag = new MYLLandingPageFragment();
        sMaxCount = maxCount;
        sFieldType = fieldType;
        sValue = value;
        return frag;
    }

    public interface PostListingListner {
        public void postListing(int fieldType, String value);
        public void setToolbarForFragment();
    }


    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (PostListingListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PostListingListner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,  Bundle savedInstanceState) {
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
                mCallback.postListing(sFieldType,String.valueOf(etValue.getText()));
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.postListing(sFieldType,String.valueOf(etValue.getText()));
                Utils.hideKeyboard(getActivity());
            }
        });



        etValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StringTokenizer st = new StringTokenizer(String.valueOf(etValue.getText()).trim());
                int wordsLeft = sMaxCount - st.countTokens();
                tvWordCount.setText(Html.fromHtml(getString(R.string.wordCount, wordsLeft)));
            }
        });
    }

    private void setUpViews(View view) {
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        etValue = (EditText)view.findViewById(R.id.etValue);
        tvWordCount = (TextView)view.findViewById(R.id.tvWordCount);
        tvWordCount.setText(getString(R.string.wordCount,sMaxCount));
        ivHint = (ImageView)view.findViewById(R.id.ivHint);
        mCallback.setToolbarForFragment();
        tvDone = (TextView) getActivity().findViewById(R.id.tvToolbarTitle);
        if(sFieldType==0) {
            tvTitle.setText(R.string.myl_title);
            etValue.setHint(R.string.myl_titleHint);
        }
        else if(sFieldType==1) {
            tvTitle.setText(R.string.myl_summary);
            etValue.setHint(R.string.myl_summaryHint);
        }
        if(!sValue.isEmpty())
            etValue.setText(sValue);
    }
}
