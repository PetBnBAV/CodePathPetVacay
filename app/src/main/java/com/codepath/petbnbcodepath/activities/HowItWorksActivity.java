package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.codepath.petbnbcodepath.interfaces.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.HowItWorksFragment;


public class HowItWorksActivity extends FragmentActivity implements FragmentCommunicator {

    FragmentPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


    }

    @Override
    public void startExploring() {

    }

    @Override
    public void login() {

        Intent i = new Intent(HowItWorksActivity.this, LoginSignupActivity.class);
        startActivity(i);

    }

    @Override
    public void listYourSpace() {

    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
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
                    return HowItWorksFragment.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return HowItWorksFragment.newInstance(1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return HowItWorksFragment.newInstance(2);
                case 3: // Fragment # 1 - This will show SecondFragment
                    return HowItWorksFragment.newInstance(3);
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
