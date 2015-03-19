package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.codepath.petbnbcodepath.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.WhyHostFragment;


public class WhyHostActivity extends FragmentActivity implements FragmentCommunicator {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_why_host);

        ViewPager vwpPager = (ViewPager) findViewById(R.id.vpager);
        adapterViewPager = new MyPgrAdapter(getSupportFragmentManager());
        vwpPager.setAdapter(adapterViewPager);
    }

    @Override
    public void startExploring() {

    }

    @Override
    public void login() {
        Intent i = new Intent(WhyHostActivity.this, LoginSignupActivity.class);
        startActivity(i);

    }

    @Override
    public void listYourSpace() {
        Intent intentThree = new Intent(WhyHostActivity.this, ListYourSpaceActivity.class);
        startActivity(intentThree);

    }


    public static class MyPgrAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPgrAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return WhyHostFragment.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return WhyHostFragment.newInstance(1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return WhyHostFragment.newInstance(2);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }



}

