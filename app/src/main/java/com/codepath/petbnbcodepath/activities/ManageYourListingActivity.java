package com.codepath.petbnbcodepath.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.MYLAddressFragment;
import com.codepath.petbnbcodepath.fragments.MYLLandingPageFragment;
import com.codepath.petbnbcodepath.fragments.MYLPriceFragment;
import com.codepath.petbnbcodepath.helpers.Constants;

public class ManageYourListingActivity extends ActionBarActivity implements MYLLandingPageFragment.PostListingListner,MYLPriceFragment.PriceListingListner,
        MYLAddressFragment.AddressListingListner{

    FragmentTransaction ft;
    private final String TAG = ManageYourListingActivity.class.getSimpleName();
    private LinearLayout stickyProgressBar;
    private LinearLayout llMYLImage;

    private ImageView coverImage;
    private TextView tvPhotoCount;
    private CheckBox cbPhoto;

    private LinearLayout llMYLTitle;
    private TextView tvMYLTitle;
    private CheckBox cbTitle;

    private LinearLayout llMYLSummary;
    private TextView tvMYLSummary;
    private CheckBox cbSummary;

    private LinearLayout llMYLPrice;
    private TextView tvMYLPrice;
    private CheckBox cbPrice;

    private LinearLayout llMYLAddress;
    private TextView tvMYLAddress;
    private CheckBox cbAddress;

    private TextView tvOptionalDetails;
    private Activity sActivity;
    private TextView tvStickyButton;
    private boolean stickyButtonEnabled = true;//TODO make it enable only in certain situation
    final static int max_word_count_title = 35;
    final static int max_word_count_summary = 50;
    MYLLandingPageFragment mylLandingPage;
    MYLPriceFragment mylPriceFragment;
    MYLAddressFragment mylAddress;

    //TODO make enums
    private final int titleIndex = 0;
    private final int summaryIndex = 1;

    int petType,houseType;
    String city;
    int petCount, petSize,playground;

    String mTitle,mSummary;
    String[] coverImages;
    int mCost;
    String mAddress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_your_listing);
        sActivity = this;
        Intent intent = getIntent();
        petType = intent.getIntExtra(Constants.petTypeKey,-1);
        houseType = intent.getIntExtra(Constants.houseTypeKey,-1);
        city = intent.getStringExtra(Constants.cityKey);
        petCount = intent.getIntExtra(Constants.petCountKey,-1);
        petSize = intent.getIntExtra(Constants.petSizeKey,-1);
        playground =intent.getIntExtra(Constants.playgroundKey,-1);
        setupViews();
        setupViewListeners();
    }

    private void setupViews() {
        stickyProgressBar = (LinearLayout) findViewById(R.id.stickyProgressBar);
//        String sumbitButtonText  = getResources().getQuantityString(R.string.countdown_unit,3);
        llMYLImage = (LinearLayout) findViewById(R.id.llMYLImage);
        coverImage = (ImageView) findViewById(R.id.coverImage);
        tvPhotoCount = (TextView) findViewById(R.id.tvPhotoCount);
        cbPhoto = (CheckBox) findViewById(R.id.cbPhoto);

        llMYLTitle = (LinearLayout) findViewById(R.id.llMYLTitle);

        tvMYLTitle = (TextView) findViewById(R.id.tvMYLTitle);
        cbTitle = (CheckBox) findViewById(R.id.cbTitle);

        llMYLSummary = (LinearLayout) findViewById(R.id.llMYLSummary);
        tvMYLSummary = (TextView) findViewById(R.id.tvMYLSummary);
        cbSummary = (CheckBox) findViewById(R.id.cbSummary);

        llMYLPrice = (LinearLayout) findViewById(R.id.llMYLPrice);
        tvMYLPrice = (TextView) findViewById(R.id.tvMYLPrice);
        cbPrice = (CheckBox) findViewById(R.id.cbPrice);

        llMYLAddress = (LinearLayout) findViewById(R.id.llMYLAddress);
        tvMYLAddress = (TextView) findViewById(R.id.tvMYLAddress);
        cbAddress = (CheckBox) findViewById(R.id.cbAddress);

        tvOptionalDetails = (TextView) findViewById(R.id.optionalDetails);
        tvStickyButton = (TextView) findViewById(R.id.tvNext);
    }
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    };

    public void setupViewListeners() {
        tvStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!stickyButtonEnabled)
                    return;
                //Post the listing
            }
        });
        llMYLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need to move camera stuff to fragment
                                Intent intent = new Intent(ManageYourListingActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        llMYLTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylLandingPage = MYLLandingPageFragment.getInstance(sActivity,ManageYourListingActivity.max_word_count_summary,titleIndex,(String)tvMYLTitle.getText());
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylLandingPage);
                ft.commit();
            }
        });
        llMYLSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylLandingPage = MYLLandingPageFragment.getInstance(sActivity,ManageYourListingActivity.max_word_count_summary,summaryIndex,(String)tvMYLSummary.getText());
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylLandingPage);
                ft.commit();
            }
        });
        llMYLPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price;
                try{ price = Integer.parseInt((String) tvMYLPrice.getText());}
                catch (Exception e){price =0;}
                mylPriceFragment = MYLPriceFragment.getInstance(sActivity,price);
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylPriceFragment);
                ft.commit();
            }
        });

        llMYLAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = String.valueOf(tvMYLAddress.getText());
                mylAddress = MYLAddressFragment.getInstance(sActivity,address);
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylAddress);
                ft.commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_your_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void postListing(int fieldType,String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        FragmentTransaction  ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylLandingPage);
        ft1.commit();
        if(fieldType==0){
            cbTitle.setChecked(!value.isEmpty());
            tvMYLTitle.setText(value);
            mTitle = value;
        }
        else if(fieldType==1){
            cbSummary.setChecked(!value.isEmpty());
            tvMYLSummary.setText(value);
            mSummary=value;
        }
    }

    @Override
    public void postListing(String value) {
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylPriceFragment);
        ft1.commit();
        try {
            cbPrice.setChecked(Integer.parseInt(value)>0);
            tvMYLPrice.setText(value);
        } catch (NumberFormatException e){
            cbPrice.setChecked(false);
            tvMYLPrice.setText("");
            mCost = Integer.parseInt(value);
        }
    }

    @Override
    public void addressListing(String address) {
        Toast.makeText(this, address,Toast.LENGTH_SHORT).show();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylAddress);
        ft1.commit();
        cbAddress.setChecked(!address.isEmpty());
        tvMYLAddress.setText(address);
        mAddress=address;
    }
}
