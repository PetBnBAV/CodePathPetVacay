package com.codepath.petbnbcodepath.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.codepath.petbnbcodepath.models.Listing;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by gangwal on 3/9/15.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    public String tempCoverPicture= "";
    public Listing currentListing;
    ArrayList<String> mImageUrlList;
    boolean mIsPreview;
    public int[] mImages = new int[] {
            R.drawable.pet_sitter1,
            R.drawable.pet_sitter2,
            R.drawable.pet_sitter3,
            R.drawable.pet_sitter4,
            R.drawable.pett_sitter5,
    };

    public ImagePagerAdapter(Context context, ArrayList<String> imageUrlList,boolean isPreview){
        this.context = context;
        this.mImageUrlList = imageUrlList;
        this.mIsPreview = isPreview;
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Context context = getActivity();
        ImageView imageView = new ImageView(context);
//            int padding = context.getResources().getDimensionPixelSize(
//                    R.dimen.padding_medium);
//            imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setAdjustViewBounds(true);
        //TODO dummy logic, needs to be updated
        if(tempCoverPicture==null)
            tempCoverPicture= "";
//        if(position==0 && !tempCoverPicture.isEmpty()){
//            imageView.setImageResource(0);
//
//            Picasso.with(context)
//                    .load(tempCoverPicture)
//                    .fit()
//                    .into(imageView);
//        }
//        else {
        String selectedImageUri = mImageUrlList.get(position);
        if(mIsPreview && (!selectedImageUri.contains("content://"))){
            Uri uri = null;
//            if (selectedImageUri != null) {
//                uri = Uri.parse(selectedImageUri);
//
//            }
            Picasso.with(context)
                    .load(new File(selectedImageUri))
                    .placeholder(R.drawable.default_photo_bg)
                    .into(imageView);
        }
        else {
            Picasso.with(context)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.default_photo_bg)
                    .into(imageView);
        }
//        }
//        else {
//            Uri uri = null;
//            if (selectedImageUri != null) {
//                uri = Uri.parse(selectedImageUri);
//
//            }
//            Picasso.with(context)
//                    .load(new File(selectedImageUri))
//                    .placeholder(R.drawable.default_photo_bg)
//                    .into(imageView);
//        }

        ((ViewPager) container).addView(imageView, 0);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check to see if we are in List or Detail page
                if(currentListing!=null)
                    Utils.gotoDetailsPage(context,currentListing);
            }
        });




        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
