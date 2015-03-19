package com.codepath.petbnbcodepath.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.HistoryPageFragment;
import com.codepath.petbnbcodepath.fragments.HistoryPageUserFragment;
import com.codepath.petbnbcodepath.fragments.LandingPageFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.ParseUser;

/**
 * Created by anuscorps23 on 3/12/15.
 */


public class FragmentPageAdapter extends FragmentPagerAdapter implements IconTabProvider {
    final int PAGE_COUNT = 2;
    private int tabIcons[] = {R.drawable.ic_petvacay, R.drawable.ic_history};

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return LandingPageFragment.newInstance(Constants.currLatLng.getLatitude(),
                                                   Constants.currLatLng.getLongitude());
        } else if (position == 1) {

            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                return HistoryPageUserFragment.newInstance(position + 1);
            }
            else {
                return HistoryPageFragment.newInstance(position + 1);

            }
        }

        return LandingPageFragment.newInstance(Constants.currLatLng.getLatitude(),
                                               Constants.currLatLng.getLongitude());

    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}



