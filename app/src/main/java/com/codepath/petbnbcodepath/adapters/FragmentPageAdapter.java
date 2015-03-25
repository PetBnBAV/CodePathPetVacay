package com.codepath.petbnbcodepath.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.HistoryPageFragment;
import com.codepath.petbnbcodepath.fragments.HistoryPageUserFragment;
import com.codepath.petbnbcodepath.fragments.PostingsListFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.ParseUser;

/**
 * Created by anuscorps23 on 3/12/15.
 */


public class FragmentPageAdapter extends FragmentPagerAdapter implements IconTabProvider {
    final int PAGE_COUNT = 2;
    private int tabIcons[] = {R.drawable.ic_petvacay, R.drawable.ic_history};
    private Activity mActivity;

    public FragmentPageAdapter(FragmentManager fm, Activity mActivity) {

        super(fm);
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            Bundle bundle = new Bundle();
            bundle.putDouble(Constants.LATITUDE, Constants.currLatLng.getLatitude());
            bundle.putDouble(Constants.LONGITUDE, Constants.currLatLng.getLongitude());
            PostingsListFragment postingsListFragment = PostingsListFragment.getInstance(mActivity);
            postingsListFragment.setArguments(bundle);
            /*return LandingPageFragment.newInstance(Constants.currLatLng.getLatitude(),
                                                   Constants.currLatLng.getLongitude());*/
            return postingsListFragment;
        } else if (position == 1) {

            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                return HistoryPageUserFragment.newInstance(position + 1);
            }
            else {
                return HistoryPageFragment.newInstance(position + 1);

            }
        }

        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.LATITUDE, Constants.currLatLng.getLatitude());
        bundle.putDouble(Constants.LONGITUDE, Constants.currLatLng.getLongitude());
        PostingsListFragment postingsListFragment = PostingsListFragment.getInstance(mActivity);
        postingsListFragment.setArguments(bundle);

        /*return LandingPageFragment.newInstance(Constants.currLatLng.getLatitude(),
                                               Constants.currLatLng.getLongitude());*/
        return postingsListFragment;

    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

}



