package com.codepath.petbnbcodepath.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.HistoryPageFragment;
import com.codepath.petbnbcodepath.fragments.HistoryPageUserFragment;
import com.codepath.petbnbcodepath.fragments.PostingsListFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.ParseUser;

/**
 * Created by anuscorps23 on 4/4/15.
 */

public class MyPagerAdapter extends SmartFragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    final int PAGE_COUNT = 2;
    private int tabIcons[] = {R.drawable.ic_petvacay, R.drawable.ic_history};
    private Activity mActivity;
    PostingsListFragment postingsListFragment;
    public MyPagerAdapter(FragmentManager fm, Activity mActivity) {

        super(fm);
        this.mActivity = mActivity;

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment currentFragment = postingsListFragment;
        if(position==1){
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                currentFragment = HistoryPageUserFragment.newInstance(position + 1);
            }
            else {
                currentFragment = HistoryPageFragment.newInstance(position + 1);

            }
        }
        else if(position ==0){
            if(postingsListFragment == null)
            {
                Bundle bundle = new Bundle();
                bundle.putDouble(Constants.LATITUDE, Constants.currLatLng.getLatitude());
                bundle.putDouble(Constants.LONGITUDE, Constants.currLatLng.getLongitude());
                postingsListFragment = PostingsListFragment.getInstance(mActivity);
                postingsListFragment.setArguments(bundle);
                currentFragment = postingsListFragment;
            }
        }
        else {
            Log.i("MyPager"," " + position);
        }


//        }
//        Bundle bundle = new Bundle();
//        bundle.putDouble(Constants.LATITUDE, Constants.currLatLng.getLatitude());
//        bundle.putDouble(Constants.LONGITUDE, Constants.currLatLng.getLongitude());
//        PostingsListFragment postingsListFragment = PostingsListFragment.getInstance(mActivity);
//        postingsListFragment.setArguments(bundle);
//
//        /*return LandingPageFragment.newInstance(Constants.currLatLng.getLatitude(),
//                                               Constants.currLatLng.getLongitude());*/
//        return postingsListFragment;
    return currentFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

}
