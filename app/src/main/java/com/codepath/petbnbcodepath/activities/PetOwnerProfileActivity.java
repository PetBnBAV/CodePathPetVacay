package com.codepath.petbnbcodepath.activities;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.ExpandableListAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;
import com.squareup.picasso.Picasso;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class PetOwnerProfileActivity extends ActionBarActivity {

    private static final String TAG = "PETOWNERPROFILE";

    private ImageView ivProfilePic;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvDropDates;
    private TextView tvPickDates;
    private TextView tvMsgFrom;
    private TextView tvMessage;

    private Button btnAccept;
    private Button btnDecline;

    private ImageView ivMsgOpen;

    private ExpandableListView lvPrice;

    private LinearLayout llBtnAcceptDecline;


    // This is the id of the pet owner who made the request
    private String objectId;
    private String bookingId;
    private ParseUser petOwnerUser;
    private String whoami;
    private int cost;
    private Date dropOffDate;
    private Date pickUpDate;
    private String message;
    private String firstName;

    private String fontHtmlBeg;
    private String fontHtmlEnd = "</font>";

    private String selDateFontHtmlBeg;
    private String selDateFontHtmlEnd = "</font>";

    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private int total_nights = 0;
    private int service_charge = 0;

    private DateFormat dateFormat = new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy",
                                                         Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_owner_profile);
        fontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.theme_teal) + "\">";
        selDateFontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.dark_gray)
                + "\">";


        try {
            JSONObject json = new JSONObject(getIntent().getExtras().getString("com.parse.Data"));
            // Iterate the parse keys if needed
            Iterator<String> itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equals(Constants.pushCustomDataKey)) {
                    objectId = json.getString(key);
                } else if (key.equals(Constants.bookingIdKey)) {
                    bookingId = json.getString(key);
                } else if (key.equals(Constants.whoamiKey)) {
                    whoami = json.getString(key);
                } else if (key.equals(Constants.startDateKey)) {
                    try {
                        dropOffDate = dateFormat.parse(json.getString(key));
                    } catch (java.text.ParseException e) {
                        Log.i(TAG, "Error: " + e.getMessage());
                    }
                } else if (key.equals(Constants.endDateKey)) {
                    try {
                        pickUpDate = dateFormat.parse(json.getString(key));
                    } catch (java.text.ParseException e) {
                        Log.i(TAG, "Error: " + e.getMessage());
                    }
                } else if (key.equals(Constants.costPerNightKey)) {
                    cost = json.getInt(key);
                } else if (key.equals(Constants.msgKey)) {
                    message = json.getString(key);
                }
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }
        } catch (JSONException ex) {
            Log.d(TAG, "Error: " + ex.getMessage());
        }
        setupViews();
    }

    private void setupViews() {
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvDropDates = (TextView) findViewById(R.id.tvDropDates);
        tvPickDates = (TextView) findViewById(R.id.tvPickDates);
        tvMsgFrom = (TextView) findViewById(R.id.tvMsgFrom);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnDecline = (Button) findViewById(R.id.btnDecline);

        ivMsgOpen = (ImageView) findViewById(R.id.ivMsgOpen);

        lvPrice = (ExpandableListView) findViewById(R.id.lvPrice);

        llBtnAcceptDecline = (LinearLayout) findViewById(R.id.llBtnAcceptDecline);

        if (whoami.equals(Constants.petSitterKey)) {
            ivMsgOpen.setVisibility(View.GONE);
            tvMsgFrom.setVisibility(View.GONE);
            tvMessage.setVisibility(View.GONE);
            btnDecline.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
            llBtnAcceptDecline.setVisibility(View.GONE);
        }

        if (whoami.equals(Constants.petOwnerKey) && message == null) {
            tvMessage.setVisibility(View.GONE);
            tvMsgFrom.setVisibility(View.GONE);
            ivMsgOpen.setVisibility(View.GONE);
        }

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        lvPrice.setAdapter(listAdapter);
        lvPrice.setGroupIndicator(getResources().getDrawable(R.mipmap.arrow_down_vl));
        prepareListData();


        // You have the id of the pet owner that made the request. Now, we're getting
        // the entire user
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayUserTable);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String profilePicUrl = object.getParseFile("profile_picture").getUrl();
                    firstName = object.getString(Constants.firstNameKey);
                    tvFirstName.setText(object.getString(Constants.firstNameKey));
                    tvLastName.setText(object.getString(Constants.lastNameKey));
                    if (message != null) {
                        tvMsgFrom.setText(getResources().getString(R.string.message_from) + " " +
                                          object.getString(Constants.firstNameKey) +
                                          getResources().getString(R.string.exclamation));
                    }
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int targetWidth = size.x;


                    // Resizing with 0 height, allows the height to be variable, while the width is
                    // fixed as the screen width - so the aspect ratio is maintained.
                    Picasso.with(PetOwnerProfileActivity.this)
                            .load(profilePicUrl)
                            .resize(targetWidth,
                                    (int)getResources().getDimension(R.dimen.pet_owner_profile_image))
                            .centerInside()
                            .placeholder(getResources().getDrawable(R.drawable.placeholder))
                            .into(ivProfilePic);
                    petOwnerUser = (ParseUser) object;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });

        String tvDropOffText = fontHtmlBeg + getResources().getString(R.string.drop_off)
                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                dropOffDate.getDate() + "/"
                + String.valueOf(dropOffDate.getMonth() + 1) + "/"
                + dropOffDate.getYear()
                + selDateFontHtmlEnd;
        String tvPickUpText = fontHtmlBeg + getResources().getString(R.string.pick_up)
                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                pickUpDate.getDate() + "/"
                + String.valueOf(dropOffDate.getMonth() + 1) + "/"
                + pickUpDate.getYear() + selDateFontHtmlEnd;
        tvDropDates.setText(Html.fromHtml(tvDropOffText));
        tvPickDates.setText(Html.fromHtml(tvPickUpText));
        if (message != null) {
            tvMessage.setText(message);
        }

        lvPrice.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                lvPrice.setGroupIndicator(getResources().getDrawable(R.mipmap.arrow_up_vl));
            }
        });
        lvPrice.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                lvPrice.setGroupIndicator(getResources().getDrawable(R.mipmap.arrow_down_vl));
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayBookingHistoryTable);
                query.getInBackground(bookingId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put(Constants.pendingKey, false);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e2) {
                                    if (e2 == null) {
                                        JSONObject obj;
                                        try {

                                            obj = new JSONObject();
                                            obj.put(Constants.alertKey,
                                                    getResources().getString(R.string.booking_request_accepted));

                                            // Pushing the current user (pet sitter) so the owner
                                            // can see their profile along with the booking details
                                            obj.put(Constants.pushCustomDataKey,
                                                    ParseUser.getCurrentUser().getObjectId());
                                            obj.put(Constants.costPerNightKey, cost);
                                            obj.put(Constants.startDateKey,
                                                    dropOffDate.toString());
                                            obj.put(Constants.endDateKey,
                                                    pickUpDate.toString());

                                            obj.put(Constants.whoamiKey, Constants.petSitterKey);

                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();


                                            // Push the notification to the pet owner who sent the
                                            // request
                                            query.whereEqualTo(Constants.userKey, petOwnerUser);
                                            push.setQuery(query);
                                            push.setData(obj);
                                            push.sendInBackground(new SendCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(PetOwnerProfileActivity.this,
                                                                getResources().getString(R.string.congrats_new_friend),
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.i(TAG, "Error: " + e.getMessage());
                                                    }
                                                }
                                            });
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }
                                    } else {
                                        Log.e(TAG, "Error: " + e2.getMessage());
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Error: " + e.getMessage());

                            //Toast.makeText(PetOwnerProfileActivity.this,
                                   // getResources().getString(R.string.generic_error),
                                   // Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                finish();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayBookingHistoryTable);
                query.getInBackground(bookingId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        JSONObject obj;
                                        try {

                                            obj = new JSONObject();
                                            obj.put(Constants.alertKey,
                                                    ParseUser.getCurrentUser().getString(Constants.firstNameKey) + " " +
                                                    getResources().getString(R.string.booking_request_decline));

                                            // Pushing the current user (pet sitter) so the owner
                                            // can see their profile along with the booking details
                                            obj.put(Constants.pushCustomDataKey,
                                                    ParseUser.getCurrentUser().getObjectId());
                                            obj.put(Constants.costPerNightKey, 0);
                                            obj.put(Constants.startDateKey,
                                                    dropOffDate.toString());
                                            obj.put(Constants.endDateKey,
                                                    pickUpDate.toString());

                                            obj.put(Constants.whoamiKey, Constants.petSitterKey);

                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();


                                            // Push the notification to the pet owner who sent the
                                            // request
                                            query.whereEqualTo(Constants.userKey, petOwnerUser);
                                            push.setQuery(query);
                                            push.setData(obj);
                                            push.sendInBackground(new SendCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(PetOwnerProfileActivity.this,
                                                                firstName + " " + getResources().getString(R.string.notification_submitted),
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.i(TAG, "Error: " + e.getMessage());
                                                    }
                                                }
                                            });
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                finish();
            }
        });
    }

    private void prepareListData() {

        LocalDate dropOffDateJoda = new LocalDate(dropOffDate);
        LocalDate pickUpDateJoda = new LocalDate(pickUpDate);
        total_nights = Days.daysBetween(dropOffDateJoda, pickUpDateJoda).getDays();

        if (whoami.equals(Constants.petSitterKey)) {
            service_charge = (int) Math.round(Constants.service_charge_percentage * cost
                    * total_nights);
        } else {
            service_charge = 0;
        }
        int total_cost = (cost * total_nights) + service_charge;


        // Adding child data
        listDataHeader.clear();
        listDataChild.clear();
        listDataHeader.add(String.valueOf(total_cost));


        // Adding child data
        List<String> cost_division = new ArrayList<String>();
        cost_division.add(Constants.currencySymbol + String.valueOf(cost) + " " +
                getResources().getString(R.string.times) + " " + total_nights + " " +
                getResources().getString(R.string.nights) + ";" +
                String.valueOf(cost * total_nights));
        cost_division.add(getResources().getString(R.string.service_fee) + ";" +
                String.valueOf(service_charge));

        listDataChild.put(listDataHeader.get(0), cost_division); // Header, Child data

        listAdapter.notifyDataSetChanged();
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
