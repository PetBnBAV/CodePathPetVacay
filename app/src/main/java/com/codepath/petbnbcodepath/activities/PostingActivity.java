package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.PostingsListFragment;
import com.codepath.petbnbcodepath.fragments.ToolbarWithButtonFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.helpers.Utils;

public class PostingActivity extends ActionBarActivity implements PostingsListFragment.PostingsListListener, ToolbarWithButtonFragment.ToolbarListener {
    FragmentTransaction ft;
    double latitude,longitude;
    private String TAG = PostingActivity.class.getSimpleName();
    String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        latitude = getIntent().getDoubleExtra(Constants.LATITUDE, Constants.defaultLatitude);
        longitude = getIntent().getDoubleExtra(Constants.LONGITUDE, Constants.defaultLongitude);
        Bundle bundlePostings= new Bundle();
        bundlePostings.putDouble(Constants.LATITUDE, latitude);
        bundlePostings.putDouble(Constants.LONGITUDE, longitude);

        Bundle bundleToolbar = new Bundle();
        cityName = Utils.getCityName(this, latitude,longitude);
        bundleToolbar.putString(Constants.titleKey,cityName);
        bundleToolbar.putInt(Constants.SECONADRY_ICON,R.drawable.ic_menu_mapmode);
        if(savedInstanceState==null) {
            ft = getSupportFragmentManager().beginTransaction();
            ToolbarWithButtonFragment toolbarFragment = ToolbarWithButtonFragment.getInstance(this);
            toolbarFragment.setArguments(bundleToolbar);
            ft.replace(R.id.flToolbar, toolbarFragment);
            PostingsListFragment postingsListFragment = PostingsListFragment.getInstance(this);
            postingsListFragment.setArguments(bundlePostings);
            ft.replace(R.id.flPostingList,postingsListFragment);
            ft.commit();
        }
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
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPrevious(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);    }

    @Override
    public void onSecondaryButtonClicked(View view) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra(Constants.latitude, latitude);
        i.putExtra(Constants.longitude, longitude);
        i.putExtra(Constants.locationStrKey,cityName);
        startActivity(i);
    }
}
