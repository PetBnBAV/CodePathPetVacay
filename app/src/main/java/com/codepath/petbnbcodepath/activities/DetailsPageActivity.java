package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.ImagePagerAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.viewpagers.WrapContentHeightViewPager;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class DetailsPageActivity extends ActionBarActivity {
    WrapContentHeightViewPager viewPager;
    ImageView ivSitterImage;
    ImageView ivReviewerImage;
    TextView tvSitterName;
    TextView tvReviewCountDetail;
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);


        final String firstName = getIntent().getStringExtra(Constants.firstNameKey);
        final String lastName = getIntent().getStringExtra(Constants.lastNameKey);
        final String coverPicture = getIntent().getStringExtra(Constants.coverPictureKey);
        final String reviewCount = getIntent().getStringExtra(Constants.reviewerIdKey);
        final int cost = getIntent().getIntExtra(Constants.listingCostKey,0);

        String firstReview = getIntent().getStringExtra(Constants.firstReview);
        viewPager = (WrapContentHeightViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this);
        //TODO Need pass Images to adapter
        adapter.tempCoverPicture = coverPicture;
        viewPager.setAdapter(adapter);
        tvSitterName =(TextView)findViewById(R.id.tvSitterName);
        tvSitterName.setText("Hosted by " + firstName + " " + lastName);
        tvReviewCountDetail = (TextView)findViewById(R.id.tvReviewCountDetail);
        try{
            if(Integer.parseInt(reviewCount)>0)
                tvReviewCountDetail.setText(reviewCount + " Reviews");
        }catch (NumberFormatException e){}

        tvNext = (TextView)findViewById(R.id.tvNext);
        tvNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DetailsPageActivity.this, BookingDetailsActivity.class);
                        i.putExtra(Constants.coverPictureKey, coverPicture);
                        i.putExtra(Constants.firstNameKey, firstName);
                        i.putExtra(Constants.lastNameKey, lastName);
                        i.putExtra(Constants.numReviewsKey, reviewCount);
                        i.putExtra(Constants.listingCostKey, cost);
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

        return super.onOptionsItemSelected(item);
    }
}
