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

/**
 * Created by gangwal on 3/14/15.
 */
public class LYSMoreInfoFragment extends Fragment {

    private MoreInfoListner mCallback;
    private ImageButton ibtMinus,ibtPlus;
    private TextView tvPetCount;
    private ImageButton ibtPetSmall,ibtPetMedium,ibtPetLarge;
    private ImageButton ibtPetDog,ibtPetCat,ibtPetTypeBoth;
    private ImageButton ibtPlaygroundDeck,ibtPlaygroundBackyard,ibtPlaygroundPark;
    private TextView tvStickyButton;
    private boolean stickyButtonEnabled = true;//TODO make it enable only in certain situation

    public static LYSMoreInfoFragment getInstance(Activity activity){
        LYSMoreInfoFragment frag = new LYSMoreInfoFragment();
        return frag;

    }

    public interface MoreInfoListner {
        public void getMoreInfo(int petCount, int petSize, int playground);
        public void setToolbar(String title, String secondaryTitle);
    }


    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (MoreInfoListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MoreInfoListner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,  Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_lsy_moreinfo,parent,false);
        mCallback.setToolbar(getActivity().getResources().getString(R.string.more_info),"");

        tvPetCount =  (TextView)view.findViewById(R.id.tvPetCount);
        final TextView tvPetCount1 = (TextView)view.findViewById(R.id.tvPetCount);
        ibtMinus = (ImageButton)view.findViewById(R.id.ivMinus);


        ibtPlus = (ImageButton)view.findViewById(R.id.ivPlus);
        ibtPlus.setOnClickListener(mListener);

        ibtPetSmall = (ImageButton)view.findViewById(R.id.ivPetSizeSmall);
        ibtPetSmall.setOnClickListener(mListener);

        ibtPetMedium = (ImageButton)view.findViewById(R.id.ivPetSizeMedium);
        ibtPetMedium.setOnClickListener(mListener);

        ibtPetLarge = (ImageButton)view.findViewById(R.id.ivPetSizeLarge);
        ibtPetLarge.setOnClickListener(mListener);

//        ibtPetDog = (ImageButton)view.findViewById(R.id.);
//        ibtPetCat = (ImageButton)view.findViewById(iv);
//        ibtPetTypeBoth = (ImageButton)view.findViewById(R.id.ivMinus);

        ibtPlaygroundDeck = (ImageButton)view.findViewById(R.id.ivPlaygroundDeck);
        ibtPlaygroundDeck.setOnClickListener(mListener);

        ibtPlaygroundBackyard = (ImageButton)view.findViewById(R.id.ivPlaygroundBackyard);
        ibtPlaygroundBackyard.setOnClickListener(mListener);

        ibtPlaygroundPark = (ImageButton)view.findViewById(R.id.ivPlaygroundPark);

        ibtPlaygroundPark.setOnClickListener(mListener);
        tvStickyButton = (TextView) view.findViewById(R.id.tvNext);
        setupViewListeners();
        return view;

    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    };

    public void setupViewListeners() {
        ibtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPetCount = Integer.parseInt((String) tvPetCount.getText()) - 1;
                if(newPetCount<0){
                    return;
                }
                tvPetCount.setText(String.valueOf(newPetCount));

            }
        });

        ibtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPetCount = Integer.parseInt((String) tvPetCount.getText()) + 1;
                tvPetCount.setText(String.valueOf(newPetCount));

            }
        });

        tvStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!stickyButtonEnabled)
                    return;
//                        public void getMoreInfo(int petCount,int petSize,int petType,int playground);
                int petCount = Integer.parseInt((String) tvPetCount.getText());
                //TODO need to write better logic for this. probably shiftting
                //If nothing is selected 0, if everything is selected 123
                int petSize =0;
                if(ibtPetSmall.isSelected())
                    petSize = petSize *10 + 1;//ENUMS here
                if(ibtPetMedium.isSelected())
                    petSize = petSize *10 + 2;//ENUMS here
                if(ibtPetLarge.isSelected())
                    petSize = petSize *10 + 3;//ENUMS here
                int playground = 0;
                if(ibtPlaygroundDeck.isSelected())
                    playground = playground *10 + 1;//ENUMS here
                if(ibtPlaygroundBackyard.isSelected())
                    playground = playground *10 + 2;//ENUMS here
                if(ibtPlaygroundPark.isSelected())
                    playground = playground *10 + 3;//ENUMS here
                    mCallback.getMoreInfo(petCount,petSize,playground);
            }
        });
    }


}
