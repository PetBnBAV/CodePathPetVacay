package com.codepath.petbnbcodepath.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PostingArrayAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostingActivity extends ActionBarActivity {
    ListView lvPosting;
    ArrayList<Listing> posts;

    private PostingArrayAdapter aPosts;
    private String TAG = PostingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        //TODO need to get location from intent.
        final double latitude = getIntent().getDoubleExtra("latitude", 0);
        final double longitude = getIntent().getDoubleExtra("longitude", 0);
        posts = new ArrayList<Listing>();
        getNearbyListings(latitude, longitude);
        setupView();
    }
    private void setupView() {
        lvPosting = (ListView) findViewById(R.id.lvPost);
//        for(int i=1;i<7;i++){
//            posts.add(new Listing());
//        }

    }

    //TODO Need to put it at a single place
    private void getNearbyListings(double latitude, double longitude) {
        String sitterIdKey = "sitterId";
        ParseGeoPoint currPoint = new ParseGeoPoint(latitude, longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayListingTable);
        query.whereWithinMiles(Constants.listingLatlngKey, currPoint, Constants.whereWithinMiles);
        query.include(sitterIdKey);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> listingList, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Retrieved " + listingList.size() + " listings");
                    posts.addAll(Listing.fromParseObjectList(listingList));
                    onNearbyListingsLoaded();
                } else {
                    Log.e("TAG", "Error: " + e.getMessage());

                    Toast.makeText(PostingActivity.this,
                            getResources().getString(R.string.generic_error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //TODO Uodate this as well
    public void onNearbyListingsLoaded() {
        aPosts = new PostingArrayAdapter(this,posts);
        lvPosting.setAdapter(aPosts);
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
}
