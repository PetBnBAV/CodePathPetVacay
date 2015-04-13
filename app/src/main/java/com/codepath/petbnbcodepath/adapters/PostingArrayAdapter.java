package com.codepath.petbnbcodepath.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.codepath.petbnbcodepath.models.Listing;
import com.codepath.petbnbcodepath.viewpagers.WrapContentHeightViewPager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Random;

/**
 * Created by gangwal on 3/8/15.
 */
public class PostingArrayAdapter extends RecyclerView.Adapter<PostingArrayAdapter.ViewHolder>  {

    protected class ViewHolder  extends RecyclerView.ViewHolder {
        ImageView ivSitterImage;
        ImageView ivWishlist;
        ImageView ivUserImage;
        TextView tvPostTitle;
        TextView tvPrice;
        TextView tvPostSubTitle;
        ImageView ivheart,ivheart2;
        WrapContentHeightViewPager viewPager;
        boolean isWishList = false;

        public ViewHolder(View convertView) {
            super(convertView);
            viewPager = (WrapContentHeightViewPager) convertView.findViewById(R.id.view_pager);
            ivWishlist = (ImageView)convertView.findViewById(R.id.ivWishlist);
            ivSitterImage = (ImageView)convertView.findViewById(R.id.ivSitterImage);
            tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
            tvPostSubTitle = (TextView)convertView.findViewById(R.id.tvPostSubTitle);
            tvPostTitle = (TextView)convertView.findViewById(R.id.tvPostTitle);
            ivheart = (ImageView)convertView.findViewById(R.id.ivheart);
            ivheart2 = (ImageView)convertView.findViewById(R.id.ivheart2);

        }
    }

    private FragmentManager mFragmentManger;
    Activity mActivity;
    Listing listing;
    //TODO Need to check if making viewholder final effect the performance
    ImageView ivWishlist;
    List<Listing> mlistings;

    public PostingArrayAdapter(FragmentActivity activity,List<Listing> listings){
        this.mlistings = listings;
        mActivity = activity;
        mFragmentManger = activity.getSupportFragmentManager();
    }

    @Override
    public int getItemCount() {
        return mlistings.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);

        return  new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final Listing currentListing = mlistings.get(position);
        final ViewHolder viewHolderF = viewHolder;

        viewHolder.tvPrice.setText("$ " + currentListing.getCost());
        //TODO Need to update this
        String city = (currentListing.getCityState()==null)?"San Francisco":currentListing.getCityState();
        viewHolder.tvPostTitle.setText(currentListing.getTitle());
//        viewHolder.tvPostSubTitle.setText("Entire Home - "+currentListing.getNumReviews() +" Reviews - " +city);
        viewHolder.tvPostSubTitle.setText("Entire Home - "+randInt() +" Reviews - " +city);

        viewHolder.ivSitterImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(mActivity.getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(mActivity)
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
                viewHolderF.ivWishlist.startAnimation(anim);
                if(!currentListing.isWishlist())
                    viewHolderF.ivWishlist.setImageResource(R.drawable.wishlist_heart_selected);
                else
                    viewHolderF.ivWishlist.setImageResource(R.drawable.wishlist_heart_unselected);
                currentListing.setWishlist(!currentListing.isWishlist);

            }
        });
        final ImageView ivheart = viewHolder.ivheart;
        final ImageView ivheart2 = viewHolder.ivheart2;

        ivWishlist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(ivheart2, "alpha", 0.2f)
                                .setDuration(1000),
                        ObjectAnimator.ofFloat(ivheart, "alpha", 0.2f)
                                .setDuration(1000),
                        ObjectAnimator.ofFloat(ivheart2, "scaleX", 0.2f, 1.0f)
                                .setDuration(1000),
                        ObjectAnimator.ofFloat(ivheart2, "scaleY", 0.2f, 1.0f)
                                .setDuration(1000)
                );
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofFloat(ivheart2, "alpha", 0.0f)
                                .setDuration(0),
                        ObjectAnimator.ofFloat(ivheart, "alpha", 0.0f)
                                .setDuration(0));
                AnimatorSet set3 = new AnimatorSet();
                set3.playSequentially(set, animatorSet);
                set3.start();
                viewHolderF.ivWishlist.startAnimation(anim);
                if(!currentListing.isWishlist())
                    viewHolderF.ivWishlist.setImageResource(R.drawable.wishlist_heart_selected);
                else
                    viewHolderF.ivWishlist.setImageResource(R.drawable.wishlist_heart_unselected);
                currentListing.setWishlist(!currentListing.isWishlist);                return true;
            }
        });

        viewHolder.ivSitterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing,false,viewHolder.ivSitterImage);
            }
        });

        //TODO need to have a icon on action bar.

        ImagePagerAdapter adapter = new ImagePagerAdapter(mActivity,currentListing.getImageUrlList(),false);
        //TODO Need pass Images to adapter
        adapter.tempCoverPicture = currentListing.getCoverPictureUrl();
        adapter.currentListing = currentListing;
        viewHolder.viewPager.setAdapter(adapter);

        //Setting onClick for most of the item in the view

//        viewHolder.viewPager.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });

        viewHolder.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing,false,viewHolder.ivSitterImage);
            }
        });

        viewHolder.tvPostTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing,false,viewHolder.ivSitterImage);
            }
        });
        viewHolder.tvPostSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing,false,viewHolder.ivSitterImage);
            }
        });

    }

//    public PostingArrayAdapter(FragmentActivity activity, List<Listing> listings,String dummy) {
//        super(activity, R.layout.item_post, listings);
//        mActivity = activity;
//        mlistings = listings;
//        mFragmentManger = activity.getSupportFragmentManager();    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final Listing currentListing = getItem(position);
//        listing = getItem(position);
//        final ViewHolder viewHolder;
//        if(convertView==null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
//            convertView.setTag(viewHolder);
//            //View pager need to
//            viewHolder.viewPager = (WrapContentHeightViewPager) convertView.findViewById(R.id.view_pager);
//            viewHolder.ivWishlist = (ImageView)convertView.findViewById(R.id.ivWishlist);
//            viewHolder.ivSitterImage = (ImageView)convertView.findViewById(R.id.ivSitterImage);
//            viewHolder.tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
//            viewHolder.tvPostSubTitle = (TextView)convertView.findViewById(R.id.tvPostSubTitle);
//            viewHolder.tvPostTitle = (TextView)convertView.findViewById(R.id.tvPostTitle);
//
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.tvPrice.setText("$ " + currentListing.getCost());
//        //TODO Need to update this
//        String city = (currentListing.getCityState()==null)?"San Francisco":currentListing.getCityState();
//        viewHolder.tvPostTitle.setText(currentListing.getTitle());
//        viewHolder.tvPostSubTitle.setText("Entire Home - "+currentListing.getNumReviews() +" Reviews - " +city);
//
//        viewHolder.ivSitterImage.setImageResource(0);
//        Transformation transformation = new RoundedTransformationBuilder()
//                .borderColor(mActivity.getResources().getColor(R.color.white))
//                .borderWidthDp(1)
//                .cornerRadiusDp(25)
//                .oval(false)
//                .build();
//
//        Picasso.with(getContext())
//                .load(currentListing.getCoverPictureUrl())
//                .placeholder(R.drawable.sample_profile)
//                .fit()
//                .transform(transformation)
//                .into(viewHolder.ivSitterImage);
//
//        final Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.scale);
//        ivWishlist = viewHolder.ivWishlist;
//        viewHolder.ivWishlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.ivWishlist.startAnimation(anim);
//                viewHolder.ivWishlist.setImageResource(R.drawable.wishlist_heart_selected);
//            }
//        });
//        viewHolder.ivSitterImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoDetailsPage(currentListing);
//            }
//        });
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"TOAST",Toast.LENGTH_SHORT).show();
//                        gotoDetailsPage(currentListing);
//            }
//        });
//        //TODO Right now for demo we are showing both type of details page. Should not be this in real scenario.
//
//        viewHolder.tvPostTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        //TODO need to have a icon on action bar.
//        viewHolder.tvPostSubTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), MapActivity.class);
//                i.putExtra(Constants.latitude, Constants.currLatLng.getLatitude());
//                i.putExtra(Constants.longitude, Constants.currLatLng.getLongitude());
//                getContext().startActivity(i);
//            }
//        });
//        ImagePagerAdapter adapter = new ImagePagerAdapter(mActivity);
//        //TODO Need pass Images to adapter
//        adapter.tempCoverPicture = currentListing.getCoverPictureUrl();
//        viewHolder.viewPager.setAdapter(adapter);
//        return convertView;
//    }

    public static int randInt() {
        int min=10, max=40;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}