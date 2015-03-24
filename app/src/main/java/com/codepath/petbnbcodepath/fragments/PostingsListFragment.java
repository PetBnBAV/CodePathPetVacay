package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PostingArrayAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.models.Listing;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
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

    MaterialListView lvPosting;
    ArrayList<Listing> posts;

    private PostingArrayAdapter aPosts;
    private String TAG = PostingsListFragment.class.getSimpleName();

    public interface PostingsListListener {
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final double latitude = bundle.getDouble(Constants.LATITUDE, 0);
            final double longitude = bundle.getDouble(Constants.LONGITUDE, 0);
            posts = new ArrayList<Listing>();
            getNearbyListings(latitude, longitude);
        }
        lvPosting = (MaterialListView) view.findViewById(R.id.lvPost);





        return view;
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
        lvPosting.setAdapter(aPosts);
        lvPosting.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                // Do whatever you want here
            }
        });
    }
}