package com.codepath.petbnbcodepath.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.PostingsListFragment;
import com.codepath.petbnbcodepath.helpers.Constants;

public class PostingActivity extends ActionBarActivity implements PostingsListFragment.PostingsListListener{
    FragmentTransaction ft;
    private String TAG = PostingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_posting);
        final double latitude = getIntent().getDoubleExtra(Constants.LATITUDE, Constants.defaultLatitude);
        final double longitude = getIntent().getDoubleExtra(Constants.LONGITUDE, Constants.defaultLongitude);
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.LATITUDE, latitude);
        bundle.putDouble(Constants.LONGITUDE, longitude);
        if(savedInstanceState==null) {
            PostingsListFragment postingsListFragment = PostingsListFragment.getInstance(this);
            postingsListFragment.setArguments(bundle);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flPostingList,postingsListFragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCurrLoc() {

    }

    public void onEtQuerySubmit(String query) {

    }

}