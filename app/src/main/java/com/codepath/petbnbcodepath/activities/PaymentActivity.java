package com.codepath.petbnbcodepath.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codepath.petbnbcodepath.dialogs.ErrorDialogFragment;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import com.codepath.petbnbcodepath.R;
import com.stripe.android.model.Card;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends ActionBarActivity {

    private final String TAG = getClass().getName();
    private ImageButton scanButton;
    private EditText etCCNumber;
    private EditText etExpiry;
    private ImageButton btnSaveCC;

    private int MY_SCAN_REQUEST_CODE = 100;



    private Transition.TransitionListener mEnterTransitionListener;

    private String cardNumber = null;

    private static final String card_hidden = "XXXX XXXX XXXX ";

    private int edit = 0;
    private Customer editCustomer = null;

    private String fontHtmlBeg;
    private String fontHtmlEnd = "</font>";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_payment);

        // Setting tool bar as action bar because we have no action bar
        fontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.dark_gray) + "\">";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml(fontHtmlBeg +
                getResources().getString(R.string.title_activity_payment) +
                fontHtmlEnd));
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

        scanButton = (ImageButton) findViewById(R.id.scanButton);
        etCCNumber = (EditText) findViewById(R.id.etCCNumber);
        etExpiry = (EditText) findViewById(R.id.etExpiry);
        btnSaveCC = (ImageButton) findViewById(R.id.btnSaveCC);

        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                enterReveal(scanButton);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        };
        if(Utils.isLollipopOrNewer())
        {getWindow().getEnterTransition().addListener(mEnterTransitionListener);}

        setupViewListeners();

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
                            etCCNumber.setText(card_hidden + currentUser.get(Constants.last4Key));
                            etExpiry.setText(currentUser.getString(Constants.expDateKey));
                            edit = 1;
                            editCustomer = cust;
                        }
                    }
                }.execute();
        }
    }

    private void setupViewListeners() {
        etCCNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before <= s.length()) {
                    if ((s.length() == 4) ||
                            (s.length() == 9) ||
                            (s.length() == 14)) {
                        etCCNumber.setText(etCCNumber.getText().toString() + " ");
                        etCCNumber.setSelection(etCCNumber.getText().toString().length());
                    }

                    if (s.length() == 19 && etExpiry.getText().toString().length() == 7) {
                        enterReveal(btnSaveCC);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.US);
                Calendar expiryDateDate = Calendar.getInstance();
                try {
                    expiryDateDate.setTime(formatter.parse(input));
                } catch (ParseException e) {
                    if (s.length() == 2) {
                        int month = Integer.parseInt(input);
                        if (month > 0 && month <= 12) {
                            etExpiry.setText(etExpiry.getText().toString() + " / ");
                            etExpiry.setSelection(etExpiry.getText().toString().length());
                        }
                    }  else if (s.length() == 1) {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            etExpiry.setText("0" + etExpiry.getText().toString() + "/");
                            etExpiry.setSelection(etExpiry.getText().toString().length());
                        }
                    } else if (s.length() == 7) {
                        if (etCCNumber.getText().toString().length() == 19) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etCCNumber.getWindowToken(), 0);
                            enterReveal(btnSaveCC);
                        }
                    }
                }
            }
        });

        btnSaveCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardNumber == null) {
                    cardNumber = "";
                    String[] cardNumberParts = etCCNumber.getText().toString().split(" ");
                    for (int i = 0; i < cardNumberParts.length; i++) {
                        cardNumber = cardNumber + cardNumberParts[i];
                    }
                }

                int expMonth = Integer.parseInt(etExpiry.getText().toString().split("/")[0].trim());
                int expYear = Integer.parseInt(etExpiry.getText().toString().split("/")[1].trim());

                final Card card = new Card(
                        cardNumber,
                        expMonth,
                        expYear, null);

                boolean validation = card.validateCard();
                if (validation) {
                    new Stripe().createToken(
                            card,
                            Constants.STRIPE_PUBLISHABLE_KEY,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    final ParseUser currentUser = ParseUser.getCurrentUser();
                                    com.stripe.Stripe.apiKey = Constants.STRIPE_SECRET_KEY;
                                    //Customer Parameters HashMap
                                    final Map<String, Object> customerParams = new HashMap();
                                    customerParams.put("description", "Customer for " +
                                            currentUser.getUsername());
                                    customerParams.put("card", token.getId());
                                    final Map<String, Object> customerParamsFinal = customerParams;

                                    new AsyncTask<Void, Void, Void>() {
                                        Customer cust;

                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            try {
                                                if (edit == 0) {
                                                    cust = Customer.create(customerParamsFinal);
                                                } else {
                                                    cust = editCustomer;
                                                    editCustomer.update(customerParamsFinal);
                                                }
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
                                                currentUser.put(Constants.tokenIdKey,
                                                        cust.getId());
                                                currentUser.put(Constants.last4Key,
                                                                                  card.getLast4());
                                                currentUser.put(Constants.cardTypeKey,
                                                                                   card.getType());
                                                currentUser.put(Constants.expDateKey,
                                                        card.getExpMonth() + " / "
                                                                + card.getExpYear());

                                                currentUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        if (e != null) {
                                                            Toast.makeText(PaymentActivity.this,
                                                                    getResources().getString(
                                                                            R.string.unable_save_card),
                                                                    Toast.LENGTH_SHORT).show();
                                                            Log.e(TAG, "Card not saved! " +
                                                                    e.getMessage());
                                                        } else {
                                                            Toast.makeText(PaymentActivity.this,
                                                                    getResources().getString(
                                                                            R.string.saved_card),
                                                                    Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent();
                                                            setResult(RESULT_OK, i);
                                                            finish();
                                                            overridePendingTransition(R.anim.hold,
                                                                    R.anim.pull_out_to_left);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }.execute();
                                }

                                public void onError(Exception error) {
                                    handleError(error.getLocalizedMessage());
                                }
                            });
                } else if (!card.validateNumber()) {
                    handleError("The card number that you entered is invalid");
                } else if (!card.validateExpiryDate()) {
                    handleError("The expiration date that you entered is invalid");
                } else if (!card.validateCVC()) {
                    handleError("The CVC code that you entered is invalid");
                } else {
                    handleError("The card details that you entered are invalid");
                }
            }
        });
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }

    private void enterReveal(View v) {
        // previously invisible view
        final View myView = v;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;
        if (Utils.isLollipopOrNewer()) {
            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (Utils.isLollipopOrNewer()) {
                        getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.setDuration(600);
            anim.start();
        }
    }

    private void exitReveal(View v) {
        // previously visible view
        final View myView = v;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;
        if (Utils.isLollipopOrNewer()) {
            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);

                    // Finish the activity after the exit transition completes.
                    supportFinishAfterTransition();
                }
            });

            // start the animation
            anim.start();
        }
    }

    public void onScanPress(View v) {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = scanResult.getRedactedCardNumber();
            cardNumber = scanResult.getFormattedCardNumber();

            etCCNumber.setText(resultStr);

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr = scanResult.expiryMonth + "/" + scanResult.expiryYear;
                etExpiry.setText(resultStr);
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }
        } else {
            resultStr = "Scan was canceled.";
        }
        enterReveal(btnSaveCC);

    }

    @Override
    public void onBackPressed() {
        exitReveal(scanButton);
        exitReveal(btnSaveCC);
        finish();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            exitReveal(scanButton);
            exitReveal(btnSaveCC);
            finish();
            overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
