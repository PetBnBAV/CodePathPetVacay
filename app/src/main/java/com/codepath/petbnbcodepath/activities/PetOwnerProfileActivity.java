package com.codepath.petbnbcodepath.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PetOwnerProfileActivity extends ActionBarActivity {

    private static final String TAG = "PETOWNERPROFILE";

    private ImageView ivProfilePic;
    private TextView tvFirstName;
    private TextView tvLastName;
    private Button btnAccept;

    private String objectId;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_owner_profile);


        try {
            JSONObject json = new JSONObject(getIntent().getExtras().getString("com.parse.Data"));
            //Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
            // Iterate the parse keys if needed
            Iterator<String> itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                // Extract custom push data
                if (key.equals(Constants.pushCustomDataKey)) {
                    // Handle push notification by invoking activity directly
                    //launchSomeActivity(context, json.getString(key));
                    // OR trigger a broadcast to activity
                    //triggerBroadcastToActivity(context);
                    // OR create a local notification
                    //createNotification(context);
                    objectId = json.getString(key);
                }
                if (key.equals("bookingId")) {
                    // Handle push notification by invoking activity directly
                    //launchSomeActivity(context, json.getString(key));
                    // OR trigger a broadcast to activity
                    //triggerBroadcastToActivity(context);
                    // OR create a local notification
                    //createNotification(context);
                    bookingId = json.getString(key);
                }
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }
        } catch (JSONException ex) {
            Log.d(TAG, "JSON failed!");
        }
        setupViews();
    }

    private void setupViews() {
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        btnAccept = (Button) findViewById(R.id.btnAccept);

        //String objectId = getIntent().getStringExtra(Constants.pushCustomDataKey);
        Log.i(TAG, "and here " + objectId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayUserTable);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "goes here");
                    tvFirstName.setText(object.getString(Constants.firstNameKey));
                    tvLastName.setText(object.getString(Constants.lastNameKey));
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());

                    Toast.makeText(PetOwnerProfileActivity.this,
                            getResources().getString(R.string.generic_error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //tvFirstName.setText("Yadda");
        //tvLastName.setText("Yadda");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayBookingHistoryTable);
                query.getInBackground(bookingId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put("pending", false);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e2) {
                                    if (e2 == null) {
                                        ParsePush push = new ParsePush();
                                        ParseQuery query = ParseInstallation.getQuery();

                                        Log.i(TAG, "return push " + objectId);
                                        query.whereEqualTo("user", objectId);
                                        push.setQuery(query);
                                        push.setMessage("Your booking has been accepted!");
                                        push.sendInBackground();
                                    } else {
                                        Log.e(TAG, "Error: " + e2.getMessage());
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Error: " + e.getMessage());

                            Toast.makeText(PetOwnerProfileActivity.this,
                                    getResources().getString(R.string.generic_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pet_owner_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
