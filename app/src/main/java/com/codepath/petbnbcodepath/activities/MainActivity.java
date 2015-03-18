package com.codepath.petbnbcodepath.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.FragmentPageAdapter;
import com.codepath.petbnbcodepath.fragments.LandingPageFragment;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


public class MainActivity extends ActionBarActivity implements
                                                    GoogleApiClient.ConnectionCallbacks,
                                                    LocationListener,
                                                    GoogleApiClient.OnConnectionFailedListener,
                                                    LandingPageFragment.OnLandingPageListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mProfileTasks;

    private LinearLayout llLogInSignUp;
    private ImageView ivImageView;
    private ImageView ivAppIconImg;
    private ImageView ivProfile;
    private ImageView ivMail;
    private ImageView ivFavorites;
    private TextView tvLogInSignUp;
    private String[] mOptionMenu;
    private ViewPager viewPager;

    boolean loggedIn = false;


    private GoogleApiClient mGoogleApiClient;

    private long UPDATE_INTERVAL = 5 * 60000;  /* 60 secs */
    //private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private long FASTEST_INTERVAL = 5 * 60000;


    /*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setUpListeners();
        setUpNavigationDrawer();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        /*viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        checkIfUserLoggedIn();*/


    }



    public void initialize()
    {

        mProfileTasks = getResources().getStringArray(R.array.profileLoggedOut_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.lvRight_Drawer);
        llLogInSignUp = (LinearLayout) findViewById(R.id.llLoginSignUp);
        ivImageView =  (ImageView) findViewById(R.id.image_view);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivMail = (ImageView) findViewById(R.id.ivMail);
        ivFavorites = (ImageView) findViewById(R.id.ivFavorites);


        mOptionMenu = new String[] { getString(R.string.howitworks_fragment),
                getString(R.string.whyhost_fragment),
                getString(R.string.listyourspace_fragment),
                getString(R.string.help_fragment) };

        tvLogInSignUp = (TextView) findViewById(R.id.tvLogInSignUp);
        ivAppIconImg = (ImageView) findViewById(R.id.ivAppIconImg);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mProfileTasks));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }




    public void setUpListeners() {

        llLogInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (loggedIn) {
                    Toast.makeText(MainActivity.this, "Log out", Toast.LENGTH_SHORT).show();
                    logOut();

                } else {
                    Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show();
                    myLogin();
                }

            }
        });


        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!loggedIn) {
                    Toast.makeText(MainActivity.this, "Logo Clicked", Toast.LENGTH_SHORT).show();
                    myLogin();
                }

            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                ivProfile.setVisibility(View.INVISIBLE);


                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    ivProfile.setVisibility(View.VISIBLE);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                    ivProfile.setVisibility(View.INVISIBLE);
                    mDrawerList.invalidate();
                }
            }
        });

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Mail clicked", Toast.LENGTH_SHORT).show();
            }
        });


        ivFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Favorites clicked", Toast.LENGTH_SHORT).show();
            }
        });


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);

            }
        });


    }




    public void checkIfUserLoggedIn()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            userIsLoggedIn();


        } else {
            // show the signup or login screen
            userIsLoggedOut();
        }
    }



    public  void userIsLoggedIn()
    {
        ivMail.setImageResource(R.drawable.ic_mail_loggedin);
        ivMail.setEnabled(true);
        ivFavorites.setImageResource(R.drawable.ic_favorites_loggedin);
        ivFavorites.setEnabled(true);
        //ivSettings.setImageResource(R.drawable.ic_settings_loggedin);
        //ivSettings.setEnabled(true);

        tvLogInSignUp.setText("");
        tvLogInSignUp.setText(R.string.AskToLogout);

         getProfilePicture();

        loggedIn = true;

    }



    public void  getProfilePicture()
    {

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

            final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("username",currentUser.getUsername());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e != null) {

                        if(object.get("profile_picture") != null) {

                            final ParseFile file = (ParseFile) object.get("profile_picture");
                            if (file != null) {
                                file.getDataInBackground(new GetDataCallback() {


                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {
                                            if (file.getUrl() != null) {


                                                //Insert profile picture pertaining to each user
                                                //Insert profile picture pertaining to each user
                                                Transformation transformation = new RoundedTransformationBuilder()
                                                        .borderColor(Color.LTGRAY)
                                                        .borderWidthDp(3)
                                                        .cornerRadiusDp(30)
                                                        .oval(true)
                                                        .build();


                                                Picasso.with(getApplicationContext())
                                                        .load(file.getUrl())
                                                        .fit()
                                                        .transform(transformation)
                                                        .into(ivAppIconImg);


                                                ivAppIconImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                                            }

                                        } else {
                                            // something went wrong
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                       }

                    } else {
                        Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();

                    }
                }

            });
        }

    }



    public  void userIsLoggedOut()
    {
        ivMail.setImageResource(R.drawable.ic_mail_loggedout);
        ivMail.setEnabled(false);
        ivFavorites.setImageResource(R.drawable.ic_favorites_loggedout);
        ivFavorites.setEnabled(false);
        //ivSettings.setImageResource(R.drawable.ic_settings_loggedout);
        //ivSettings.setEnabled(false);

        tvLogInSignUp.setText(R.string.AskToLogin);
        ivAppIconImg.setImageResource(R.drawable.ic_app_icon);
        ivAppIconImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

        loggedIn = false;

    }



    private void myLogin() {
        Intent i = new Intent(MainActivity.this, LoginSignupActivity.class);
        startActivity(i);

        /*ParseUser.logInInBackground("anuscorps23@gmail.com", "welcome1", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(MainActivity.this, "I am logged in", Toast.LENGTH_LONG).show();
                    userIsLoggedIn();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    userIsLoggedOut();
                    Toast.makeText(MainActivity.this, "Log in failed", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }


    private void logOut()
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to log out?");

        // Setting Positive "Log Out" Btn
        alertDialog.setPositiveButton("LOG OUT",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                //Log out
                ParseUser.logOut();
                userIsLoggedOut();
            }
        });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to execute after dialog
            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            }
        });

        // Showing Alert Dialog
        alertDialog.show();

    }




    public void setUpNavigationDrawer()
    {
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mProfileTasks));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_profile, R.string.drawer_open,R.string.drawer_close)  {
            public void onDrawerClosed(View view) {
                ivProfile.setVisibility(View.VISIBLE);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerStateChanged(int newState) {
                if(newState == DrawerLayout.STATE_DRAGGING){
                    if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                        ivProfile.setVisibility(View.INVISIBLE);

                    }else{
                        ivProfile.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //ivProfile.setVisibility(View.INVISIBLE);
                super.onDrawerSlide(drawerView, slideOffset);
            }


            public void onDrawerOpened(View drawerView) {
                ivProfile.setVisibility(View.INVISIBLE);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }



    private void selectItem(int position) {

        switch (position) {
            case 0:
                Intent intentOne = new Intent(MainActivity.this, HowItWorksActivity.class);
                startActivity(intentOne);
                break;
            case 1:
                Intent intentTwo= new Intent(MainActivity.this, WhyHostActivity.class);
                startActivity(intentTwo);
                break;
            case 2:
                //mFragment = new ListYourSpaceFragment();
                Intent intentThree = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intentThree);
                break;

            case 3:
                //mFragment = new HelpFragment();
                break;
        }

       /* if(mFragment != null) {


            // Create the transaction
            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();

            // Replace the content of the container
            fts.replace(R.id.content_frame, mFragment);

            // Append this transaction to the backstack
            fts.addToBackStack("lastNavigationDrawerItem");

            // Commit the changes
            fts.commit();
        }*/

        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //TODO: Figure out when to unlock it!!
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Toast.makeText(this, Double.toString(mCurrentLocation.getLatitude()) + "," +
                Double.toString(mCurrentLocation.getLongitude()), Toast.LENGTH_LONG).show();
        Constants.currLatLng = new ParseGeoPoint(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude());
        viewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        checkIfUserLoggedIn();
        Toast.makeText(this, "new! " + Double.toString(mCurrentLocation.getLatitude()) + "," +
                Double.toString(mCurrentLocation.getLongitude()), Toast.LENGTH_LONG).show();

        /*landingPageFragment = LandingPageFragment.newInstance(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_landing_page, landingPageFragment);
        ft.commit();*/

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Constants.currLatLng = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginSignup(View view) {
        Intent i = new Intent(MainActivity.this, LoginSignupActivity.class);
        startActivity(i);
    }

    public void onEtQuerySubmit(String query) {
        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
    }

    public void onlvLandingPageItemClick(double latitude, double longitude) {
        Intent i = new Intent(MainActivity.this, PostingActivity.class);

        i.putExtra(Constants.latitude, Constants.currLatLng.getLatitude());
        i.putExtra(Constants.longitude, Constants.currLatLng.getLongitude());
        startActivity(i);
    }

}
