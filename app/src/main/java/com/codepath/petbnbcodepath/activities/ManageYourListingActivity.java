package com.codepath.petbnbcodepath.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.MYLAddressFragment;
import com.codepath.petbnbcodepath.fragments.MYLLandingPageFragment;
import com.codepath.petbnbcodepath.fragments.MYLPriceFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.codepath.petbnbcodepath.models.Listing;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ManageYourListingActivity extends ActionBarActivity implements MYLLandingPageFragment.PostListingListner,MYLPriceFragment.PriceListingListener,
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
    //Need to remove default, after demo.
    private ParseGeoPoint mGeoPoint = new ParseGeoPoint(Constants.defaultLatitude,Constants.defaultLongitude);;

    private TextView tvOptionalDetails;
    private Activity sActivity;
    private Button btStickyButton;
    private boolean stickyButtonEnabled = true;//TODO make it enable only in certain situation
    final static int max_word_count_title = 35;
    final static int max_word_count_summary = 50;

    private int stepsLeft =5;
    MYLLandingPageFragment mylLandingPage;
    MYLPriceFragment mylPriceFragment;
    MYLAddressFragment mylAddress;

    Toolbar toolbar;
    TextView tvToolbatTitle;
    TextView tvToolbarSecondaryTitle;

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

    ParseUser currentUser = ParseUser.getCurrentUser();

    final int RESULT_PICTURES_ADDED = 60;

    Listing listing = new Listing();
    String selectedImageUri;
    ArrayList<String> imageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_manage_your_listing);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolbatTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarSecondaryTitle = (TextView) findViewById(R.id.tvToolbarSecondaryTitle);
        setSupportActionBar(toolbar);
        sActivity = this;
        Intent intent = getIntent();
        petType = intent.getIntExtra(Constants.petTypeKey,-1);
        houseType = intent.getIntExtra(Constants.houseTypeKey,-1);
        city = intent.getStringExtra(Constants.cityKey);
        petCount = intent.getIntExtra(Constants.petCountKey,-1);
        petSize = intent.getIntExtra(Constants.petSizeKey,-1);
        playground =intent.getIntExtra(Constants.playgroundKey,-1);
        setupViews();
        setupListing();
        setupViewListeners();
    }
    private void setupListing() {
        listing.setPetType(petType);
        listing.setHomeType(houseType);
        listing.setCityState(city);
        boolean hasPets = petCount>0?true:false;
        listing.setHasPets(hasPets);
        //TODO Pet Size & Playground not implemented
        //listing.setPetSize(petSize);
        //playground =intent.getIntExtra(Constants.playgroundKey,-1);
        }

    private void setupViews() {
        stickyProgressBar = (LinearLayout) findViewById(R.id.stickyProgressBar);
//        String sumbitButtonText  = MessageFormat.format(getBaseContext().getString(R.string.countdown_unit), stepsLeft);
//        getResources().getQuantityText(R.string.countdown_unit,stepsLeft);
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
        btStickyButton = (Button) findViewById(R.id.btNext);
        updateProgress(stepsLeft);
    }
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    };

    public void setupViewListeners() {
        btStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stickyButtonEnabled)
                    return;
                //Post the listing
            }
        });
        llMYLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need to move camera stuff to fragment
                Intent intent = new Intent(ManageYourListingActivity.this, CameraActivity.class);
                startActivityForResult(intent, RESULT_PICTURES_ADDED);
            }
        });
        llMYLTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylLandingPage = MYLLandingPageFragment.getInstance(sActivity, ManageYourListingActivity.max_word_count_summary, titleIndex, (String) tvMYLTitle.getText());
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylLandingPage);
                ft.commit();
            }
        });
        llMYLSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylLandingPage = MYLLandingPageFragment.getInstance(sActivity, ManageYourListingActivity.max_word_count_summary, summaryIndex, (String) tvMYLSummary.getText());
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylLandingPage);
                ft.commit();
            }
        });
        llMYLPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price;
                try {
                    price = Integer.parseInt((String) tvMYLPrice.getText());
                } catch (Exception e) {
                    price = 0;
                }
                mylPriceFragment = MYLPriceFragment.getInstance(sActivity, price);
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylPriceFragment);
                ft.commit();
            }
        });

        llMYLAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = String.valueOf(tvMYLAddress.getText());
                mylAddress = MYLAddressFragment.getInstance(sActivity, address);
                ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.flMYL, mylAddress);
                ft.commit();
            }
        });


        btStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] photo = new byte[0];
                try {
                    Uri uri = Uri.parse(selectedImageUri);
                    photo = Utils.readBytesFromURI(ManageYourListingActivity.this, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String currentUserId = ParseUser.getCurrentUser().getObjectId();

                final ParseFile file = new ParseFile("coverImage_" + currentUserId + "_1.jpg", photo);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        final ParseObject listingPictures = new ParseObject(Constants.PetVacayListingPictures);
                        listingPictures.put(Constants.listingPictureItemKeys[0], file);
                        for (int i = 1; i < imageUrlList.size(); i++) {
                            try {
                                final ParseFile file = new ParseFile("coverImage_" + currentUserId + "_" + (i + 1) + ".jpg", Utils.readBytesFromURI(ManageYourListingActivity.this, Uri.parse(imageUrlList.get(i))));
                                final String key = Constants.listingPictureItemKeys[i];
                                file.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        listingPictures.put(key, file);
                                        listingPictures.saveInBackground();

                                    }
                                });
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        final String objectIdListingPicture;
                        if (currentUser != null) {

                            final ParseObject posting = new ParseObject(Constants.petVacayListingTable);
                            posting.put(Constants.costKey, mCost);
                            posting.put(Constants.titleKey, mTitle);
                            posting.put(Constants.descriptionKey, mSummary);
                            posting.put(Constants.hasPetsKey, true);//TODO need to update the table or UI
                            posting.put(Constants.petTypeKey, petType);
                            posting.put(Constants.homeTypeKey, houseType);
                            posting.put(Constants.listingPicturesKey, listingPictures);
                            //TODO need to update petSize, playground, city
                            ParseGeoPoint geoPoint = Utils.getLocationFromAddress(ManageYourListingActivity.this, mAddress);
                            //TODO If the address is not a valid address, notify user.
                            if (geoPoint == null)
                                geoPoint = new ParseGeoPoint(Constants.defaultLatitude, Constants.defaultLongitude);
                            posting.put(Constants.latlngKey, geoPoint);
                            posting.put(Constants.sitterIdKey, currentUser);
                            posting.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e2) {
//                                    Toast.makeText(ManageYourListingActivity.this, "Posted a posting with title " + mTitle, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        gotoPreview();
                    }
                });
            }
        });


        tvToolbarSecondaryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPreview();
            }
        });

    }
    private void disableStickyButton() {
//        btStickyButton.setEnabled(false);
        btStickyButton.setAlpha(Constants.btnDisabledAlpha);
    }

    private void enableStickyButton(){
//        btStickyButton.setEnabled(true);
        btStickyButton.setAlpha(Constants.btnEnabledAlpha);
    }

    public void gotoPreview(){
        Utils.gotoDetailsPage(this, listing, true);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
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

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarForFragment() {
        tvToolbatTitle.setText(R.string.done_title);
        tvToolbatTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_checkmark_photo,0,0,0);
        tvToolbarSecondaryTitle.setVisibility(View.INVISIBLE);
    }

    public void setDefaultToolbar() {
        tvToolbatTitle.setText(R.string.list_your_space_title);
        tvToolbatTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_backinblack,0,0,0);
        tvToolbarSecondaryTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void postListing(int fieldType,String value) {
        setDefaultToolbar();
        FragmentTransaction  ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylLandingPage);
        ft1.commit();
        if(fieldType==0){
            cbTitle.setChecked(!value.isEmpty());
            mTitle = value;
            if(!value.isEmpty()){updateProgress(--stepsLeft);}
            tvMYLTitle.setText(value);
            listing.setTitle(mTitle);
        }
        else if(fieldType==1){
            cbSummary.setChecked(!value.isEmpty());
            mSummary=value;
            if(!value.isEmpty()){updateProgress(--stepsLeft);}
            tvMYLSummary.setText(value);
            listing.setDescription(mSummary);
        }
    }

    @Override
    public void postListing(String value) {
        setDefaultToolbar();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylPriceFragment);
        ft1.commit();
        try {
            cbPrice.setChecked(Integer.parseInt(value)>0);
            mCost = Integer.parseInt(value);
            if(Integer.parseInt(value)>0){updateProgress(--stepsLeft);}
            tvMYLPrice.setText(value);
            listing.setCost(mCost);
        } catch (NumberFormatException e){
            cbPrice.setChecked(false);
            tvMYLPrice.setText("");
            listing.setCost(mCost);
        }
    }

    @Override
    public void addressListing(String address) {
        setDefaultToolbar();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.hide(mylAddress);
        ft1.commit();
        cbAddress.setChecked(!address.isEmpty());
        mAddress=address;
        if(!address.isEmpty()){updateProgress(--stepsLeft);}
        tvMYLAddress.setText(address);
        mGeoPoint = Utils.getLocationFromAddress(ManageYourListingActivity.this, mAddress);
        //TODO If the address is not a valid address, notify user.
        if(mGeoPoint==null)
            mGeoPoint = new ParseGeoPoint(Constants.defaultLatitude,Constants.defaultLongitude);
        listing.setLatLng(mGeoPoint);
    }

    public void updateProgress(int steps){
        int count = stickyProgressBar.getChildCount();
        btStickyButton.setText(getString(R.string.countdown_unit_, Math.max(0,steps)));
        View v = null;
//        if(count-steps<=1){
//            stickyButtonEnabled = true;
//            return;
//        }
        for(int i=1; i<count-steps; i++) {
           try {
               v = stickyProgressBar.getChildAt(i);
               v.setBackgroundColor(getResources().getColor(R.color.teal));
           }catch (Exception e){}
        }

        if(selectedImageUri==null||mTitle==null||mSummary==null||mAddress==null||mCost<=0)
        {
            disableStickyButton();
        }
        else {
            enableStickyButton();
            btStickyButton.setText(getString(R.string.complete_profile));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_PICTURES_ADDED && resultCode == RESULT_OK && null != data) {
            imageUrlList = data.getExtras().getStringArrayList(Constants.COVER_PICTURES_URL);
            listing.setImageUrlList(imageUrlList);
            for(String imageUrl : imageUrlList){
                if(imageUrl!=null) {
                    selectedImageUri = imageUrl;
                    break;
                }
            }
            ImageView image = (ImageView) findViewById(R.id.coverImage);

            //Picture directly from camera
            if(selectedImageUri!=null) {
                if (selectedImageUri.contains("content://")) {
                    Picasso.with(getApplicationContext())
                            .load(selectedImageUri)
                            .placeholder(R.drawable.default_photo_bg)
                            .into(image);
                } else {
                    Uri uri = Uri.parse(selectedImageUri);
                    Picasso.with(getApplicationContext())
                            .load(new File(selectedImageUri))
                            .placeholder(R.drawable.default_photo_bg)
                            .into(image);
                }
            }
            cbPhoto.setChecked(true);
            updateProgress(--stepsLeft);
        }
    }

}
