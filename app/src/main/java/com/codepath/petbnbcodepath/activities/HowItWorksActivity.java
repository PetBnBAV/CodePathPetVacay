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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.HowItWorksFragment;
import com.codepath.petbnbcodepath.interfaces.FragmentCommunicator;


public class HowItWorksActivity extends FragmentActivity implements FragmentCommunicator {

    FragmentPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_how_it_works);



        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                TextView tvHSWTitle = (TextView) page.findViewById(R.id.tvHSWTitle);
                TextView tvHSWText = (TextView) page.findViewById(R.id.tvHSWText);
                LinearLayout linearLayoutHSW = (LinearLayout) page.findViewById(R.id.linearLayoutHSW);
                Button btnHSWSkip = (Button) page.findViewById(R.id.btnHSWSkip);
                Button btnHSWBack = (Button) page.findViewById(R.id.btnHSWBack);
                int pageWidth = page.getWidth();


                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(1);

                }

                else if (position <= 1) { // [-1,1]
                    //ivHWSImage.setTranslationX(position * pageWidth / 16);
                    tvHSWTitle.setTranslationX(position * pageWidth * 2);
                    tvHSWText.setTranslationX(position * pageWidth / 2);
                    linearLayoutHSW.setTranslationX(position * pageWidth / 1);
                    btnHSWSkip.setTranslationX(position * pageWidth / 2);
                    btnHSWBack.setTranslationX(position * pageWidth / 4);

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
