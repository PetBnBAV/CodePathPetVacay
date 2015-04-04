package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PlacesAutoCompleteAdapter;
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

/**
 * Created by gangwal on 3/21/15.
 */
public class PostingsListFragment extends Fragment {


    private static Activity sActivity;
    private PostingsListListener mCallback;

    RecyclerView lvPosting;
    ArrayList<Listing> posts;

    private PostingArrayAdapter aPosts;
    private String TAG = PostingsListFragment.class.getSimpleName();

    private AutoCompleteTextView etSearch;
    private TextView tvCurrLoc;

    public interface PostingsListListener {
        public void onEtQuerySubmit(String query);
        public void onCurrLoc();
    }

    public static PostingsListFragment getInstance(Activity activity){
        PostingsListFragment frag = new PostingsListFragment();
        sActivity = activity;
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (PostingsListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PostingsListListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_posting_list,parent,false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        animationDrawable = (AnimationDrawable)progressBar.getIndeterminateDrawable();
        showProgressBar();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final double latitude = bundle.getDouble(Constants.LATITUDE, 0);
            final double longitude = bundle.getDouble(Constants.LONGITUDE, 0);
            posts = new ArrayList<Listing>();

            getNearbyListings(latitude, longitude);
        }
        lvPosting = (RecyclerView) view.findViewById(R.id.lvPost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        lvPosting.setLayoutManager(layoutManager);
        lvPosting.setHasFixedSize(true);
        etSearch = (AutoCompleteTextView) view.findViewById(R.id.etSearch);
        etSearch.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
        tvCurrLoc = (TextView) view.findViewById(R.id.tvCurrLoc);


        setupViewListeners();


        return view;
    }

    private void setupViewListeners() {

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager in  =(InputMethodManager) getActivity().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = (String) parent.getItemAtPosition(position);
                etSearch.setText("");
                mCallback.onEtQuerySubmit(query);
            }
        });

        tvCurrLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCurrLoc();
            }
        });
    }

    private AnimationDrawable animationDrawable;
    private ProgressBar progressBar;

    public void showProgressBar() {
        animationDrawable.start();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        animationDrawable.stop();
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Add near by listing to ArrayAdapter
     * @param latitude
     * @param longitude
     */
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
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Update he arrayAdapter and set it to listview.
     */
    public void onNearbyListingsLoaded() {
        aPosts = new PostingArrayAdapter((android.support.v4.app.FragmentActivity) sActivity,posts);
        hideProgressBar();
        lvPosting.setAdapter(aPosts);
    }
}