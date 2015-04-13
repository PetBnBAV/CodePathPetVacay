package com.codepath.petbnbcodepath.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.ExpandableListAdapter;
import com.codepath.petbnbcodepath.fragments.DatePickerDialog;
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
import com.squareup.picasso.Picasso;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import com.stripe.android.model.Card;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;


public class BookingDetailsActivity extends ActionBarActivity implements
                                                      DatePickerDialog.OnDatePickerDialogListener {

    private static final String TAG = "BOOKINGDETAILACTIVITY";

    private static final int BOOKING_ACTIVITY_RETURN = 17;

    private ImageView ivCoverPicture;

    private TextView tvSummary;
    private TextView tvNumReviews;
    private TextView tvCost;
    private TextView tvSayHello;
    private TextView tvSayHelloSub;

    private Button btnSelDropDates;
    private Button btnSelPickDates;
    private Button btnBook;
    private Button btnAddPayment;

    private ExpandableListView lvPrice;

    private EditText etMsgHost;

    private String coverPictureUrl;
    private String firstName;
    private String lastName;
    private String objectId;
    private int numReviews;
    private int cost;

    private int total_nights = 0;
    private int service_charge;

    private String fontHtmlBeg;
    private String fontHtmlEnd = "</font>";
    private String selDateFontHtmlBeg;
    private String selDateFontHtmlEnd = "</font>";

    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private DatePickerDialog datePickerDialog;

    private Date booking_dropOffDate;
    private Date booking_pickUpDate;

    private TextView tvCardType;
    private ImageButton btnEditCard;
    private ImageButton btnDeleteCard;
    private ImageView ivCardLogo;
    private TextView tvLast4;


    public void onDatesSelected(Date dropOffDate, Date pickUpDate) {

        // To be able to use Joda's days between library, we need to convert the dates from
        // java.util.Date to LocaleDate
        LocalDate dropOffDateJoda = new LocalDate(dropOffDate);
        LocalDate pickUpDateJoda = new LocalDate(pickUpDate);
        total_nights = Days.daysBetween(dropOffDateJoda, pickUpDateJoda).getDays();

        // + 1 to print the correct month because indexing starts at 0 internally
        String btnDropOffText = fontHtmlBeg + getResources().getString(R.string.drop_off)
                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                dropOffDate.getDate() + "/"
                + String.valueOf(dropOffDate.getMonth() + 1) + "/"
                + dropOffDate.getYear()
                + selDateFontHtmlEnd;
        String btnPickUpText = fontHtmlBeg + getResources().getString(R.string.pick_up)
                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                pickUpDate.getDate() + "/"
                + String.valueOf(dropOffDate.getMonth() + 1) + "/"
                + pickUpDate.getYear() + selDateFontHtmlEnd;
        btnSelDropDates.setText(Html.fromHtml(btnDropOffText));
        btnSelPickDates.setText(Html.fromHtml(btnPickUpText));
        prepareListData();

        booking_dropOffDate = dropOffDate;
        booking_pickUpDate = pickUpDate;

        // Once the dates are selected
        // and the user has entered their payment information
        // the booking button can be enabled
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null && currentUser.getString(Constants.tokenIdKey) != null) {
            btnBook.setEnabled(true);
            btnBook.setAlpha(Constants.btnEnabledAlpha);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_booking_details);
        fontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.theme_teal) + "\">";
        selDateFontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.dark_gray)
                + "\">";

        // Setting tool bar as action bar because we have no action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml(selDateFontHtmlBeg +
                getResources().getString(R.string.title_activity_booking_details) +
                selDateFontHtmlEnd));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for(int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);

            // Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(
                        getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
            }
        }


        coverPictureUrl = getIntent().getStringExtra(Constants.coverPictureKey);
        firstName = getIntent().getStringExtra(Constants.firstNameKey);
        lastName = getIntent().getStringExtra(Constants.lastNameKey);
        numReviews = getIntent().getIntExtra(Constants.numReviewsKey, 0);
        cost = getIntent().getIntExtra(Constants.listingCostKey, 0);
        objectId = getIntent().getStringExtra(Constants.objectIdKey);

        datePickerDialog = DatePickerDialog.newInstance();

        setupViews();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
    }

    private void setupViews() {

        ivCoverPicture = (ImageView) findViewById(R.id.ivCoverPicture);
        ivCardLogo = (ImageView) findViewById(R.id.ivCardLogo);
        tvSummary = (TextView) findViewById(R.id.tvSummary);
        tvNumReviews = (TextView) findViewById(R.id.tvNumReviews);
        tvCost = (TextView) findViewById(R.id.tvCost);
        tvSayHello = (TextView) findViewById(R.id.tvSayHello);
        tvSayHelloSub = (TextView) findViewById(R.id.tvSayHelloSub);
        tvCardType = (TextView) findViewById(R.id.tvCardType);
        tvLast4 = (TextView) findViewById(R.id.tvLast4);
        btnSelDropDates = (Button) findViewById(R.id.btnSelDropDates);
        btnSelPickDates = (Button) findViewById(R.id.btnSelPickDates);
        btnBook = (Button) findViewById(R.id.btnBook);
        btnAddPayment = (Button) findViewById(R.id.btnAddPayment);
        btnEditCard = (ImageButton) findViewById(R.id.btnEditCard);
        btnDeleteCard = (ImageButton) findViewById(R.id.btnDeleteCard);
        lvPrice = (ExpandableListView) findViewById(R.id.lvPrice);
        etMsgHost = (EditText) findViewById(R.id.etMsgHost);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null || currentUser.getString(Constants.tokenIdKey) == null) {
            ivCardLogo.setVisibility(View.INVISIBLE);
            tvCardType.setVisibility(View.INVISIBLE);
            tvLast4.setVisibility(View.INVISIBLE);
            btnEditCard.setVisibility(View.INVISIBLE);
            btnEditCard.setEnabled(false);
            btnDeleteCard.setVisibility(View.INVISIBLE);
            btnDeleteCard.setEnabled(false);
        } else {
            btnAddPayment.setEnabled(false);
            btnAddPayment.setVisibility(View.INVISIBLE);

            tvLast4.setText(currentUser.getString(Constants.last4Key));
            String cardType = currentUser.getString(Constants.cardTypeKey);
            tvCardType.setText(cardType);
            if (cardType.equals(Card.MASTERCARD)) {
                Drawable cardDrawable = getResources().getDrawable(R.mipmap.mastercard);
                int currWidth = cardDrawable.getIntrinsicWidth();
                int currHeight = cardDrawable.getIntrinsicHeight();
                int origAspectRatio = currWidth / currHeight;
                int targetHeight = (int) getResources().getDimension(R.dimen.card_logo_height);
                int targetWidth = origAspectRatio * targetHeight;

                Picasso.with(this).load(R.mipmap.mastercard).resize(targetWidth, targetHeight)
                                                              .into(ivCardLogo);

            } else if (cardType.equals(Card.VISA)) {
                Drawable cardDrawable = getResources().getDrawable(R.mipmap.visa);
                int currWidth = cardDrawable.getIntrinsicWidth();
                int currHeight = cardDrawable.getIntrinsicHeight();
                int origAspectRatio = currWidth / currHeight;
                int targetHeight = (int) getResources().getDimension(R.dimen.card_logo_height);
                int targetWidth = origAspectRatio * targetHeight;

                Picasso.with(this).load(R.mipmap.visa).resize(targetWidth, targetHeight).centerInside()
                        .into(ivCardLogo);
            }
        }

        // Button to book is disabled till dates are selected
        btnBook.setEnabled(false);
        btnBook.setAlpha(Constants.btnDisabledAlpha);

        Picasso.with(this).load(coverPictureUrl).into(ivCoverPicture);
        tvSummary.setText(firstName + " " + lastName);
        if (numReviews == 1) {
            tvNumReviews.setText(String.valueOf(numReviews) + " " +
                                                        getResources().getString(R.string.review));
        } else {
            tvNumReviews.setText(String.valueOf(numReviews) + " " +
                                                       getResources().getString(R.string.reviews));
        }
        tvCost.setText(Constants.currencySymbol + String.valueOf(cost));
        tvSayHello.setText(getResources().getString(R.string.say_hello_1) + " " + firstName
                           + getResources().getString(R.string.say_hello_2));
        tvSayHelloSub.setText(getResources().getString(R.string.say_hello_det_1) + " " + firstName
                           + " " + getResources().getString(R.string.say_hello_det_2));
        String btnDropOffText = fontHtmlBeg + getResources().getString(R.string.drop_off)
                                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                                getResources().getString(R.string.sel_date) + selDateFontHtmlEnd;
        String btnPickUpText = fontHtmlBeg + getResources().getString(R.string.pick_up)
                + fontHtmlEnd + "<br/>" + selDateFontHtmlBeg +
                getResources().getString(R.string.sel_date) + selDateFontHtmlEnd;
        btnSelPickDates.setText(Html.fromHtml(btnPickUpText));
        btnSelDropDates.setText(Html.fromHtml(btnDropOffText));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvPrice.setAdapter(listAdapter);
        lvPrice.setGroupIndicator(getResources().getDrawable(R.mipmap.arrow_down_vl));
        prepareListData();


        setupViewListeners();
    }

    private void setupViewListeners() {

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
        btnSelDropDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getSupportFragmentManager(), TAG);
            }
        });

        btnSelPickDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getSupportFragmentManager(), TAG);
            }
        });

        etMsgHost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(
                                                                     Activity.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etMsgHost.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        etMsgHost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager in  =(InputMethodManager)getSystemService(
                                                                     Activity.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser == null) {
                    Intent i = new Intent(BookingDetailsActivity.this, LoginSignupActivity.class);
                    startActivityForResult(i, BOOKING_ACTIVITY_RETURN);
                } else {
                    Intent i = new Intent(BookingDetailsActivity.this, PaymentActivity.class);
                    startActivityForResult(i, BOOKING_ACTIVITY_RETURN);
                }
            }
        });

        btnEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingDetailsActivity.this, PaymentActivity.class);
                //startActivity(i);
                startActivityForResult(i, BOOKING_ACTIVITY_RETURN);
            }
        });

        btnDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser currentUser = ParseUser.getCurrentUser();
                final String cust_id = currentUser.getString(Constants.tokenIdKey);

                com.stripe.Stripe.apiKey = "sk_live_jE00zEtVGdtR7ZEQj11kZwNx";
                if (cust_id != null) {
                    new AsyncTask<Void, Void, Void>() {
                        Customer cust;

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                cust = Customer.retrieve(cust_id);
                                cust.delete();
                            } catch (AuthenticationException |
                                    InvalidRequestException |
                                    APIConnectionException |
                                    CardException | APIException e) {
                                Log.e(TAG, "Error: " + e.getMessage());
                            }
                            return null;
                        }

                        protected void onPostExecute(Void result) {
                            if (cust != null) {
                                    currentUser.remove(Constants.tokenIdKey);
                                    currentUser.remove(Constants.expDateKey);
                                    currentUser.remove(Constants.last4Key);
                                    currentUser.remove(Constants.cardTypeKey);
                                    currentUser.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Log.e(TAG, e.getMessage());
                                            }
                                        }
                                    });
                            }
                        }
                    }.execute();
                }
                ivCardLogo.setVisibility(View.INVISIBLE);
                tvCardType.setVisibility(View.INVISIBLE);
                tvLast4.setVisibility(View.INVISIBLE);
                btnEditCard.setVisibility(View.INVISIBLE);
                btnEditCard.setEnabled(false);
                btnDeleteCard.setVisibility(View.INVISIBLE);
                btnDeleteCard.setEnabled(false);

                btnAddPayment.setVisibility(View.VISIBLE);
                btnAddPayment.setEnabled(true);
            }
        });


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {

                    final ParseObject booking = new ParseObject(Constants.petVacayBookingHistoryTable);
                    booking.put(Constants.costPerNightKey, cost);
                    booking.put(Constants.startDateKey, booking_dropOffDate);
                    booking.put(Constants.endDateKey, booking_pickUpDate);
                    booking.put(Constants.pendingKey, true);
                    booking.put(Constants.ownerIdKey, currentUser);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayListingTable);
                    query.include(Constants.sitterIdKey);
                    query.getInBackground(objectId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            final ParseObject returnedObj = object;
                            if (e == null) {
                                booking.put(Constants.listingIdKey, object);
                                booking.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e2) {
                                        if (e2 == null) {
                                            JSONObject obj;
                                            try {
                                                obj = new JSONObject();
                                                obj.put(Constants.alertKey,
                                                        getResources().
                                                            getString(R.string.new_pet_request));
                                                // This is the current user, so upon receiving the
                                                // notification, the sitter is able to see
                                                // this user's (i.e. the owner's ) profile
                                                obj.put(Constants.pushCustomDataKey,
                                                         ParseUser.getCurrentUser().getObjectId());
                                                // This is the booking id, because when the pet
                                                // sitter receives the notification, if she/he
                                                // accepts, then the booking id's pending tag is
                                                // set to false. If the sitter declines, the record
                                                // is deleted.
                                                obj.put(Constants.bookingIdKey,
                                                        booking.getObjectId());
                                                obj.put(Constants.costPerNightKey, cost);
                                                obj.put(Constants.startDateKey,
                                                        booking_dropOffDate.toString());
                                                obj.put(Constants.endDateKey,
                                                        booking_pickUpDate.toString());
                                                if (etMsgHost.getText().toString().equals("")) {
                                                    obj.put(Constants.msgKey, null);
                                                } else {
                                                    obj.put(Constants.msgKey,
                                                            etMsgHost.getText().toString());
                                                }
                                                // This acts as a switch in showing/hiding
                                                // the accept/decline buttons etc
                                                obj.put(Constants.whoamiKey, Constants.petOwnerKey);

                                                ParsePush push = new ParsePush();
                                                ParseQuery query = ParseInstallation.getQuery();

                                                // Push the notification to the sitter
                                                query.whereEqualTo(Constants.userKey,
                                                        returnedObj.get(Constants.sitterIdKey));
                                                push.setQuery(query);
                                                push.setData(obj);
                                                push.sendInBackground(new SendCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(BookingDetailsActivity.this,
                                                                    getResources().getString(R.string.booking_request_submitted),
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.i(TAG, "Error: " + e.getMessage());
                                                        }
                                                    }
                                                });
                                            } catch (JSONException jsonException) {
                                                Log.e(TAG, "Error: " + jsonException.getMessage());
                                            }
                                        }
                                    }
                                });
                            } else {
                                Log.e(TAG, "Error: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Intent i = new Intent(BookingDetailsActivity.this, LoginSignupActivity.class);
                    startActivityForResult(i, BOOKING_ACTIVITY_RETURN);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == BOOKING_ACTIVITY_RETURN) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null || currentUser.getString(Constants.tokenIdKey) == null) {
                ivCardLogo.setVisibility(View.INVISIBLE);
                tvCardType.setVisibility(View.INVISIBLE);
                tvLast4.setVisibility(View.INVISIBLE);
                btnEditCard.setVisibility(View.INVISIBLE);
                btnEditCard.setEnabled(false);
                btnDeleteCard.setVisibility(View.INVISIBLE);
                btnDeleteCard.setEnabled(false);
            } else {
                ivCardLogo.setVisibility(View.VISIBLE);
                tvCardType.setVisibility(View.VISIBLE);
                tvLast4.setVisibility(View.VISIBLE);
                btnEditCard.setVisibility(View.VISIBLE);
                btnEditCard.setEnabled(true);
                btnDeleteCard.setVisibility(View.VISIBLE);
                btnDeleteCard.setEnabled(true);

                btnAddPayment.setEnabled(false);
                btnAddPayment.setVisibility(View.INVISIBLE);

                tvLast4.setText(currentUser.getString(Constants.last4Key));
                String cardType = currentUser.getString(Constants.cardTypeKey);
                tvCardType.setText(cardType);
                if (cardType.equals(Card.MASTERCARD)) {
                    Drawable cardDrawable = getResources().getDrawable(R.mipmap.mastercard);
                    int currWidth = cardDrawable.getIntrinsicWidth();
                    int currHeight = cardDrawable.getIntrinsicHeight();
                    int origAspectRatio = currWidth / currHeight;
                    int targetHeight = (int) getResources().getDimension(R.dimen.card_logo_height);
                    int targetWidth = origAspectRatio * targetHeight;

                    Picasso.with(this).load(R.mipmap.mastercard).resize(targetWidth, targetHeight)
                            .into(ivCardLogo);

                } else if (cardType.equals(Card.VISA)) {
                    Drawable cardDrawable = getResources().getDrawable(R.mipmap.visa);
                    int currWidth = cardDrawable.getIntrinsicWidth();
                    int currHeight = cardDrawable.getIntrinsicHeight();
                    int origAspectRatio = currWidth / currHeight;
                    int targetHeight = (int) getResources().getDimension(R.dimen.card_logo_height);
                    int targetWidth = origAspectRatio * targetHeight;

                    Picasso.with(this).load(R.mipmap.visa).resize(targetWidth, targetHeight).centerInside()
                            .into(ivCardLogo);
                }
                if (booking_dropOffDate != null && booking_pickUpDate != null) {
                    btnBook.setEnabled(true);
                    btnBook.setAlpha(Constants.btnEnabledAlpha);
                }
            }
        }
    }


    private void prepareListData() {

        service_charge = (int) Math.round(Constants.service_charge_percentage * cost * total_nights);
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
        getMenuInflater().inflate(R.menu.menu_booking_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

