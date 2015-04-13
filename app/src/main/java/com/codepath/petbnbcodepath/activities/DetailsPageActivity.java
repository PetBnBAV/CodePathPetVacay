package com.codepath.petbnbcodepath.activities;

import android.content.ComponentCallbacks;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.ImagePagerAdapter;
import com.codepath.petbnbcodepath.fragments.CaldroidMonthFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.viewpagers.WrapContentHeightViewPager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsPageActivity extends FragmentActivity {
    WrapContentHeightViewPager viewPager;
    ImageView ivPetType;
    ImageView ivSitterImage;
    ImageView ivReviewerImage;
    TextView tvSitterName;
    TextView tvReviewCountDetail;
    TextView tvReviewerName;
    TextView tvReviewDate;
    TextView tvReview;
    LinearLayout llReview;
    Button btNext;
    TextView tvPrice;
    TextView tvDescription;
    TextView tvPostTitle;
    ImageView ivStaticMap;
    String mFirstName;
    String mLastName;
    String mCoverPicture = null;


    private CaldroidFragment caldroidFragment;

    private void setCustomResourceForDates() {
        //TODO Once we have enough real booked dates, we will show those instead of fake dates.
        ArrayList<Date> disableDateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 4);
        disableDateList.add(cal.getTime());
        cal.add(Calendar.DATE, 1);
        disableDateList.add(cal.getTime());
        cal.add(Calendar.DATE, 3);
        disableDateList.add(cal.getTime());
        cal.add(Calendar.DATE, 4);
        disableDateList.add(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 28);
        Date toDate1 = cal.getTime();
        disableDateList.add(toDate1);
        caldroidFragment.setDisableDates(disableDateList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        caldroidFragment = new CaldroidMonthFragment();

        final String firstName;
        final String lastName;
        final String coverPicture;

        final String title = getIntent().getStringExtra(Constants.titleKey);
        final int reviewCount = getIntent().getIntExtra(Constants.reviewerIdKey, 0);
        final int cost = getIntent().getIntExtra(Constants.listingCostKey, 0);
        final String objectId = getIntent().getStringExtra(Constants.objectIdKey);
        final String firstReview = getIntent().getStringExtra(Constants.firstReview);
        final String description = getIntent().getStringExtra(Constants.descriptionKey);
        final int houseType = getIntent().getIntExtra(Constants.houseTypeKey,0);
        final int petType =  getIntent().getIntExtra(Constants.petTypeKey,0);
        final boolean hasPet = getIntent().getBooleanExtra(Constants.hasPetsKey,false);
        final ArrayList<String> imageUrlList = getIntent().getStringArrayListExtra(Constants.IMAGE_URL_LIST);
        double latitude = getIntent().getDoubleExtra(Constants.latitude,Constants.defaultLatitude);
        double longitude = getIntent().getDoubleExtra(Constants.longitude,Constants.defaultLongitude);
        String reviewDescription = getIntent().getStringExtra(Constants.firstReviewDescription);
        String firstReviewerName = getIntent().getStringExtra(Constants.firstReviewer);
        String firstReviewDate = getIntent().getStringExtra(Constants.firstReviewDate);
        String firstReviewerImage = getIntent().getStringExtra(Constants.firstReviewerImage);
        int firstReviewRating = getIntent().getIntExtra(Constants.firstReviewRating,5);

        final boolean isPreview = getIntent().getBooleanExtra(Constants.IS_PREVIEW,false);
        int visibilityRequestBtn =  (isPreview? View.GONE:View.VISIBLE);

        //Detect and show user's info
        if(!isPreview){
            mFirstName = getIntent().getStringExtra(Constants.firstNameKey);
            mLastName = getIntent().getStringExtra(Constants.lastNameKey);
            mCoverPicture = getIntent().getStringExtra(Constants.coverPictureKey);
        }
        else {

            //Show self name if isPreview
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null && isPreview) {
                mFirstName = currentUser.get(Constants.firstNameKey).toString();
                mLastName = currentUser.get(Constants.lastNameKey).toString();
                mCoverPicture = ((ParseFile) currentUser.get(Constants.profilePictureKey)).getUrl();
            }
        }

        firstName = mFirstName;
        lastName = mLastName;
        coverPicture = mCoverPicture;


        viewPager = (WrapContentHeightViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this,imageUrlList,isPreview);
        //TODO Need pass Images to adapter
        adapter.tempCoverPicture = coverPicture;
        viewPager.setAdapter(adapter);
        ivPetType = (ImageView) findViewById(R.id.ivPetType);
        tvSitterName =(TextView)findViewById(R.id.tvSitterName);
        tvSitterName.setText("Hosted by " + firstName + " " + lastName);
        tvReviewCountDetail = (TextView)findViewById(R.id.tvReviewCountDetail);
        llReview = (LinearLayout) findViewById(R.id.llReview);
        llReview.setVisibility(visibilityRequestBtn);
        try{
            if(reviewCount > 0)
                tvReviewCountDetail.setText(reviewCount + " "
                        + getResources().getString(R.string.reviews));
        }catch (NumberFormatException e){}


        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .borderWidthDp(5)
                .cornerRadiusDp(45)
                .oval(false)
                .build();


        tvReviewerName = (TextView) findViewById(R.id.tvReviewerName);
        tvReview = (TextView)findViewById(R.id.tvReview);
        tvReviewDate = (TextView) findViewById(R.id.tvReviewDate);
        ivReviewerImage = (ImageView)findViewById(R.id.ivReviewerImage);
        if(!isPreview) {
            if (firstReviewerName != null)
                tvReviewerName.setText(firstReviewerName);
            if (reviewDescription != null)
                tvReview.setText(reviewDescription);
            if (reviewDescription != null)
                Picasso.with(this)
                    .load(firstReviewerImage)
                    .placeholder(R.drawable.icon_ro_profile)
                    .fit()
                    .transform(transformation)
                    .into(ivReviewerImage);
        }
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvPrice.setText("$ "+cost);
        btNext = (Button)findViewById(R.id.btNext);
        btNext.setVisibility(visibilityRequestBtn);
        btNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DetailsPageActivity.this, BookingDetailsActivity.class);
                        i.putExtra(Constants.coverPictureKey, coverPicture);
                        i.putExtra(Constants.firstNameKey, firstName);
                        i.putExtra(Constants.lastNameKey, lastName);
                        i.putExtra(Constants.numReviewsKey, reviewCount);
                        i.putExtra(Constants.listingCostKey, cost);
                        i.putExtra(Constants.objectIdKey, objectId);
                        startActivity(i);
            }

        });
        ivSitterImage = (ImageView)findViewById(R.id.ivSitterImage);
        ivSitterImage.setImageResource(0);

        Picasso.with(this)
                .load(coverPicture)
                .fit()
                .transform(transformation)
                .into(ivSitterImage);


        tvPostTitle = (TextView) findViewById(R.id.tvPostTitle);
        tvPostTitle.setText(title);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(description);
        ivStaticMap = (ImageView)findViewById(R.id.ivStaticMap);
        //TODO Need to update the lat long with city, if lat long is null
        String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?size=600x600&zoom=15&markers=" +
                "icon:http://chart.apis.google.com/chart?chst=d_map_pin_icon%26chld=cafe%257C996600%7C" +
                latitude + "," + longitude;
        Picasso.with(this).load(staticMapUrl)
                .into(ivStaticMap);

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            caldroidFragment.setArguments(args);
        }
        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        if(!isPreview)
        {setCustomResourceForDates();}
        setUpListner();

    }

    private void setUpListner() {
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {}

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    TextView monthTitle = caldroidFragment.getMonthTitleTextView();
                    monthTitle.setTextColor(getResources().getColor(R.color.gg_text_dark_gray));
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_page, menu);
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

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
