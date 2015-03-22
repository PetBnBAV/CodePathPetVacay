package com.codepath.petbnbcodepath.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.fragments.ListingSummaryFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.models.Listing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends ActionBarActivity
                         implements GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MAPACTIVITY";

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private ArrayList<Listing> nearbyListings;
    private MyListingSummaryAdapter listingSummaryAdapter;
    private ViewPager vpPager;
    private ArrayList<Marker> markers;

    private IconGenerator iconFactoryTeal;
    private IconGenerator iconFactoryRed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_map);

        String fontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.dark_gray)
                + "\">";
        String fontHtmlEnd = "</font>";

        // Setting tool bar as action bar because we have no action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getStringExtra(Constants.locationStrKey) != null) {
            getSupportActionBar().setTitle(Html.fromHtml(fontHtmlBeg +
                    getIntent().getStringExtra(Constants.locationStrKey) +
                    fontHtmlEnd));
        } else {
            getSupportActionBar().setTitle(Html.fromHtml(fontHtmlBeg +
                    getResources().getString(R.string.title_activity_map) +
                    fontHtmlEnd));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(false);

        for(int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);

            // Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(
                        getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
            }
        }

        vpPager = (ViewPager) findViewById(R.id.vpPager);

        // Set the view pager adapter for the pager
        nearbyListings = new ArrayList<>();
        markers = new ArrayList<>();
        final double latitude = getIntent().getDoubleExtra("latitude", 0);
        final double longitude = getIntent().getDoubleExtra("longitude", 0);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude)
                            ,Constants.zoom));
                    getNearbyListings(latitude, longitude);
                }
            });
        } else {
            Log.e(TAG, "Error - Map Fragment was null!!");
        }

        iconFactoryTeal = new IconGenerator(MapActivity.this);
        iconFactoryTeal.setColor(getResources().getColor(R.color.theme_teal));
        iconFactoryTeal.setContentPadding(0, 0, 0, 0);
        iconFactoryTeal.setTextAppearance(MapActivity.this, R.style.CodeFont);

        iconFactoryRed = new IconGenerator(MapActivity.this);
        iconFactoryRed.setColor(getResources().getColor(R.color.vl_red));
        iconFactoryRed.setContentPadding(0, 0, 0, 0);
        iconFactoryRed.setTextAppearance(MapActivity.this, R.style.CodeFont);

    }

    /*@Override
    protected void onPause() {
        // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
        // Override how this activity is animated out of view
        // The new activity is kept still and this activity is pushed out to the left
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
    }

    public void onReviewCountAdded() {
        listingSummaryAdapter = new MyListingSummaryAdapter(getSupportFragmentManager(),
                nearbyListings.size(),
                nearbyListings);
        listingSummaryAdapter.notifyDataSetChanged();
    }

    public void onNearbyListingsLoaded() {
        for (int i = 0; i < nearbyListings.size(); i++) {

            // get the things you need to make the markers
            double currListLat = nearbyListings.get(i).getLatLng().getLatitude();
            double currListLong = nearbyListings.get(i).getLatLng().getLongitude();
            int currListCost = nearbyListings.get(i).getCost();

            // Add marker to map
            MarkerOptions currMarker;
            if (i == 0) {
                currMarker = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactoryRed.
                                makeIcon(Constants.currencySymbol
                                        + String.valueOf(currListCost))))
                        .position(new LatLng(currListLat, currListLong))
                        .anchor(iconFactoryRed.getAnchorU(), iconFactoryRed.getAnchorV());
            } else {
                currMarker = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactoryTeal.
                                makeIcon(Constants.currencySymbol
                                        + String.valueOf(currListCost))))
                        .position(new LatLng(currListLat, currListLong))
                        .anchor(iconFactoryTeal.getAnchorU(), iconFactoryTeal.getAnchorV());
            }
            markers.add(map.addMarker(currMarker));


        }

        // Also set fragment pager adapter for the mini fragment below
        listingSummaryAdapter = new MyListingSummaryAdapter(getSupportFragmentManager(),
                nearbyListings.size(),
                nearbyListings);
        vpPager.setAdapter(listingSummaryAdapter);
        vpPager.setOnPageChangeListener(new OnPageChangeListener() {

            // This method will be invoked when a new fragment/listing becomes selected.
            @Override
            public void onPageSelected(int position) {

                // Get the things needed from the listing to make its marker - because once
                // selected we need to make a marker which is the same as the old marker but
                // highlighted, remove the old marker, and put the new one to give the
                // highlighting effect from teal to red
                int currListCost = nearbyListings.get(position).getCost();
                double currListLat = nearbyListings.get(position).getLatLng().getLatitude();
                double currListLong = nearbyListings.get(position).getLatLng().getLongitude();
                MarkerOptions newMarker = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactoryRed.
                                makeIcon(Constants.currencySymbol
                                        + String.valueOf(currListCost))))
                        .position(new LatLng(currListLat, currListLong))
                        .anchor(iconFactoryRed.getAnchorU(), iconFactoryRed.getAnchorV());

                markers.get(position).remove();
                markers.set(position, map.addMarker(newMarker));

                // Once we're at a fragment, we need to make sure that the previously selected
                // fragment's marker is reset. Since we could've reached this marker only
                // through one of the possible 2 adjacent ones, we reset both of those regardless
                if (position < nearbyListings.size() - 1) {
                    currListCost = nearbyListings.get(position + 1).getCost();
                    currListLat = nearbyListings.get(position + 1).getLatLng().getLatitude();
                    currListLong = nearbyListings.get(position + 1).getLatLng().getLongitude();
                    newMarker = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactoryTeal.
                                    makeIcon(Constants.currencySymbol
                                            + String.valueOf(currListCost))))
                            .position(new LatLng(currListLat, currListLong))
                            .anchor(iconFactoryTeal.getAnchorU(), iconFactoryTeal.getAnchorV());
                    markers.get(position + 1).remove();
                    markers.set(position + 1, map.addMarker(newMarker));
                }

                if (position > 0) {
                    currListCost = nearbyListings.get(position - 1).getCost();
                    currListLat = nearbyListings.get(position - 1).getLatLng().getLatitude();
                    currListLong = nearbyListings.get(position - 1).getLatLng().getLongitude();
                    newMarker = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactoryTeal.
                                    makeIcon(Constants.currencySymbol
                                            + String.valueOf(currListCost))))
                            .position(new LatLng(currListLat, currListLong))
                            .anchor(iconFactoryTeal.getAnchorU(), iconFactoryTeal.getAnchorV());
                    markers.get(position - 1).remove();
                    markers.set(position - 1, map.addMarker(newMarker));
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        vpPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                ImageView ivCoverPicture = (ImageView) page.findViewById(R.id.ivCoverPicture);
                TextView tvSummary = (TextView) page.findViewById(R.id.tvSummary);
                TextView tvNumReviews = (TextView) page.findViewById(R.id.tvNumReviews);
                TextView tvCost = (TextView) page.findViewById(R.id.tvCost);
                int pageWidth = page.getWidth();


                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(1);

                }

                else if (position <= 1) { // [-1,1]
                    ivCoverPicture.setTranslationX((float)(position) * pageWidth / 1.5f);
                    tvSummary.setTranslationX((float)(position) * pageWidth / 4);
                    tvNumReviews.setTranslationX((float)(position) * pageWidth / 2);
                    tvCost.setTranslationX((float) (position) * pageWidth / 1);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(1);
                }
            }
        });
    }

    public void goToDetailView(View view) {
        int position = vpPager.getCurrentItem();
        Listing currentListing = nearbyListings.get(position);

        Intent intent = new Intent(MapActivity.this, DetailsPageActivity.class);
        intent.putExtra(Constants.firstNameKey, currentListing.getFirst_name());
        intent.putExtra(Constants.lastNameKey, currentListing.getLast_name());
        intent.putExtra(Constants.coverPictureKey, currentListing.getCoverPictureUrl());
        intent.putExtra(Constants.reviewerIdKey,currentListing.getNumReviews());
        intent.putExtra(Constants.firstReview, String.valueOf(currentListing.getFirstReview()));
        intent.putExtra(Constants.listingCostKey, currentListing.getCost());
        intent.putExtra(Constants.objectIdKey, currentListing.getObjectId());
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        for (int i = 0; i < markers.size(); i++) {
            if (marker.equals(markers.get(i))) {
                vpPager.setCurrentItem(i);
                return true;
            }
        }
        return false;
    }

    private void getNearbyListings(double latitude, double longitude) {
        String sitterIdKey = Constants.sitterIdKey;
        ParseGeoPoint currPoint = new ParseGeoPoint(latitude, longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayListingTable);
        query.whereWithinMiles(Constants.listingLatlngKey, currPoint, Constants.whereWithinMiles);
        query.include(sitterIdKey);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> listingList, ParseException e) {
                if (e == null) {
                    nearbyListings.addAll(Listing.fromParseObjectList(listingList));
                    for (int i = 0; i < listingList.size(); i++) {
                        final int pos = i;
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayReviewTable);
                        query.whereEqualTo(Constants.listingIdKey, listingList.get(i));
                        query.countInBackground(new CountCallback() {
                            public void done(int count, ParseException e) {
                                if (e == null) {
                                    nearbyListings.get(pos).setNumReviews(count);
                                    onReviewCountAdded();
                                } else {
                                    Log.i(TAG, "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                    onNearbyListingsLoaded();
                } else {
                    Log.e("TAG", "Error: " + e.getMessage());
                }
            }
        });

    }



    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
           // Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();

            /*map.setMyLocationEnabled(true);
            map.setOnMapLongClickListener(this);

            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            connectClient();*/
        } else {
            //Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
        map.setOnMarkerClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            NavUtils.navigateUpTo(this, intent);
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.aiReturnToListing) {
            Intent i = new Intent(MapActivity.this, MainActivity.class);
            startActivity(i);
            //overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyListingSummaryAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS;
        private ArrayList<Listing> nearbyListings;


        public MyListingSummaryAdapter(FragmentManager fragmentManager, int num_items,
                                       ArrayList<Listing> nearbyListings) {
            super(fragmentManager);
            NUM_ITEMS = num_items;
            this.nearbyListings = nearbyListings;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return ListingSummaryFragment.newInstance(this.nearbyListings.get(position));

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
