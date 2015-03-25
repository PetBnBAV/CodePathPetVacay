package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.ImagePagerAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.viewpagers.WrapContentHeightViewPager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class DetailsPageActivity extends ActionBarActivity {
    WrapContentHeightViewPager viewPager;
    ImageView ivSitterImage;
    ImageView ivReviewerImage;
    TextView tvSitterName;
    TextView tvReviewCountDetail;
    Button btNext;
    TextView tvPrice;
    TextView tvDescription;
    TextView tvPostTitle;
    String mFirstName;
    String mLastName;
    String mCoverPicture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
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
        tvSitterName =(TextView)findViewById(R.id.tvSitterName);
        tvSitterName.setText("Hosted by " + firstName + " " + lastName);
        tvReviewCountDetail = (TextView)findViewById(R.id.tvReviewCountDetail);
        try{
            if(reviewCount > 0)
                tvReviewCountDetail.setText(reviewCount + " "
                        + getResources().getString(R.string.reviews));
        }catch (NumberFormatException e){}
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
        ivReviewerImage = (ImageView)findViewById(R.id.ivReviewerImage);
        ivSitterImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.white))
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(coverPicture)
                .fit()
                .transform(transformation)
                .into(ivSitterImage);

        Picasso.with(this)
                .load(R.drawable.icon_ro_profile)
                .fit()
                .transform(transformation)
                .into(ivReviewerImage);
        tvPostTitle = (TextView) findViewById(R.id.tvPostTitle);
        tvPostTitle.setText(title);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(description);
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
