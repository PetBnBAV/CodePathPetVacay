package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.interfaces.FragmentCommunicator;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.WhyHostFragment;


public class WhyHostActivity extends FragmentActivity implements FragmentCommunicator {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_why_host);

        ViewPager vwpPager = (ViewPager) findViewById(R.id.vpager);
        adapterViewPager = new MyPgrAdapter(getSupportFragmentManager());
        vwpPager.setAdapter(adapterViewPager);

        vwpPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                TextView tvWHTitle = (TextView) page.findViewById(R.id.tvWHTitle);
                TextView tvWHText = (TextView) page.findViewById(R.id.tvWHText);
                Button btnWHList = (Button) page.findViewById(R.id.btnWHList);
                Button btnWHBack = (Button) page.findViewById(R.id.btnWHBack);
                int pageWidth = page.getWidth();


                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(1);

                }

                else if (position <= 1) { // [-1,1]
                    //ivHWSImage.setTranslationX(position * pageWidth / 16);
                    tvWHTitle.setTranslationX(position * pageWidth * 2);
                    tvWHText.setTranslationX(position * pageWidth / 2);
                    btnWHList.setTranslationX(position * pageWidth / 2);
                    btnWHBack.setTranslationX(position * pageWidth / 4);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(1);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
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

