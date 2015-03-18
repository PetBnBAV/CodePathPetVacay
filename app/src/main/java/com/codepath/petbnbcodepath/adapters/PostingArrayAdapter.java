package com.codepath.petbnbcodepath.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.activities.BookingDetailsActivity;
import com.codepath.petbnbcodepath.activities.DetailsPageActivity;
import com.codepath.petbnbcodepath.activities.MapActivity;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.models.Listing;
import com.codepath.petbnbcodepath.viewpagers.WrapContentHeightViewPager;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by gangwal on 3/8/15.
 */
public class PostingArrayAdapter extends ArrayAdapter<Listing> {

    private class ViewHolder{
        ImageView ivSitterImage;
        ImageView ivWishlist;
        ImageView ivUserImage;
        TextView tvPostTitle;
        TextView tvPrice;
        TextView tvPostSubTitle;
        WrapContentHeightViewPager viewPager;

    }

    private FragmentManager mFragmentManger;
    Activity mActivity;
    Listing listing;
    //TODO Need to check if making viewholder final effect the performance
    ImageView ivWishlist;


    public PostingArrayAdapter(FragmentActivity activity, List<Listing> listings) {
        super(activity, R.layout.item_post, listings);
        mActivity = activity;
        mFragmentManger = activity.getSupportFragmentManager();    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Listing currentListing = getItem(position);
        listing = getItem(position);
        final ViewHolder viewHolder;
        if(convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
            convertView.setTag(viewHolder);
            //View pager need to
            viewHolder.viewPager = (WrapContentHeightViewPager) convertView.findViewById(R.id.view_pager);
            viewHolder.ivWishlist = (ImageView)convertView.findViewById(R.id.ivWishlist);
            viewHolder.ivSitterImage = (ImageView)convertView.findViewById(R.id.ivSitterImage);
            viewHolder.tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
            viewHolder.tvPostSubTitle = (TextView)convertView.findViewById(R.id.tvPostSubTitle);
            viewHolder.tvPostTitle = (TextView)convertView.findViewById(R.id.tvPostTitle);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPrice.setText("$ " + currentListing.getCost());
        //TODO Need to update this
        String city = (currentListing.getCityState()==null)?"San Francisco":currentListing.getCityState();
        viewHolder.tvPostSubTitle.setText("Entire Home - "+currentListing.getNumReviews() +" Reviews - " +city);

        viewHolder.ivSitterImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(mActivity.getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(25)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(currentListing.getCoverPictureUrl())
                .placeholder(R.drawable.sample_profile)
                .fit()
                .transform(transformation)
                .into(viewHolder.ivSitterImage);

        final Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.scale);
        ivWishlist = viewHolder.ivWishlist;
        viewHolder.ivWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ivWishlist.startAnimation(anim);
                viewHolder.ivWishlist.setImageResource(R.drawable.wishlist_heart_selected);
            }
        });
        viewHolder.ivSitterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailsPageActivity.class);
                intent.putExtra(Constants.firstNameKey, currentListing.getFirst_name());
                intent.putExtra(Constants.lastNameKey, currentListing.getLast_name());
                intent.putExtra(Constants.coverPictureKey,currentListing.getCoverPictureUrl());
                intent.putExtra(Constants.reviewerIdKey,currentListing.getNumReviews());
                intent.putExtra(Constants.firstReview, String.valueOf(currentListing.getFirstReview()));
                getContext().startActivity(intent);
            }
        });

        //TODO Right now for demo we are showing both type of details page. Should not be this in real scenario.

        viewHolder.tvPostTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BookingDetailsActivity.class);
                i.putExtra(Constants.coverPictureKey, currentListing.getCoverPictureUrl());
                i.putExtra(Constants.firstNameKey, currentListing.getFirst_name());
                i.putExtra(Constants.lastNameKey, currentListing.getLast_name());
                i.putExtra(Constants.numReviewsKey, currentListing.getNumReviews());
                i.putExtra(Constants.listingCostKey, currentListing.getCost());
                getContext().startActivity(i);
            }
        });

        //TODO need to have a icon on action bar.
        viewHolder.tvPostSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MapActivity.class);
                i.putExtra(Constants.latitude, Constants.currLatLng.getLatitude());
                i.putExtra(Constants.longitude, Constants.currLatLng.getLongitude());
                getContext().startActivity(i);
            }
        });
        ImagePagerAdapter adapter = new ImagePagerAdapter(mActivity);
        //TODO Need pass Images to adapter
        adapter.tempCoverPicture = currentListing.getCoverPictureUrl();
        viewHolder.viewPager.setAdapter(adapter);
        return convertView;
    }
}
