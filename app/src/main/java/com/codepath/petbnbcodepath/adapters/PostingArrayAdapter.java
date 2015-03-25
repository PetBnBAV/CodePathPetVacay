package com.codepath.petbnbcodepath.adapters;

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
import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.model.Card;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Collection;
import java.util.List;

/**
 * Created by gangwal on 3/8/15.
 */
public class PostingArrayAdapter extends RecyclerView.Adapter<PostingArrayAdapter.ViewHolder> implements IMaterialListAdapter {

    @Override
    public void add(Card card) {

    }

    @Override
    public void addAll(Card... cards) {

    }

    @Override
    public void addAll(Collection<Card> cards) {

    }

    @Override
    public void remove(Card card, boolean b) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Card getCard(int i) {
        return null;
    }

    @Override
    public int getPosition(Card card) {
        return 0;
    }

    protected class ViewHolder  extends RecyclerView.ViewHolder {
        ImageView ivSitterImage;
        ImageView ivWishlist;
        ImageView ivUserImage;
        TextView tvPostTitle;
        TextView tvPrice;
        TextView tvPostSubTitle;
        WrapContentHeightViewPager viewPager;

        public ViewHolder(View convertView) {
            super(convertView);
            viewPager = (WrapContentHeightViewPager) convertView.findViewById(R.id.view_pager);
            ivWishlist = (ImageView)convertView.findViewById(R.id.ivWishlist);
            ivSitterImage = (ImageView)convertView.findViewById(R.id.ivSitterImage);
            tvPrice = (TextView)convertView.findViewById(R.id.tvPrice);
            tvPostSubTitle = (TextView)convertView.findViewById(R.id.tvPostSubTitle);
            tvPostTitle = (TextView)convertView.findViewById(R.id.tvPostTitle);
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
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Listing currentListing = mlistings.get(position);
        final ViewHolder viewHolderF = viewHolder;

        viewHolder.tvPrice.setText("$ " + currentListing.getCost());
        //TODO Need to update this
        String city = (currentListing.getCityState()==null)?"San Francisco":currentListing.getCityState();
        viewHolder.tvPostTitle.setText(currentListing.getTitle());
        viewHolder.tvPostSubTitle.setText("Entire Home - "+currentListing.getNumReviews() +" Reviews - " +city);

        viewHolder.ivSitterImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(mActivity.getResources().getColor(R.color.white))
                .borderWidthDp(1)
                .cornerRadiusDp(25)
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
                viewHolderF.ivWishlist.setImageResource(R.drawable.wishlist_heart_selected);
            }
        });
        viewHolder.ivSitterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing);
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
                Utils.gotoDetailsPage(mActivity,currentListing);
            }
        });

        viewHolder.tvPostTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing);
            }
        });
        viewHolder.tvPostSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gotoDetailsPage(mActivity,currentListing);
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

}