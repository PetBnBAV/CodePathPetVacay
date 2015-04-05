package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.BookingsHistoryAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.interfaces.FragmentCommunicator;
import com.codepath.petbnbcodepath.models.Booking;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuscorps23 on 3/12/15.
 */
public class HistoryPageUserFragment extends Fragment implements FragmentCommunicator{

    public static final String ARG_PAGE = "ARG_PAGE";

    ImageView ivBCoverPicture;
    ImageView ivBProfilePicture;
    TextView tvBTitle;
    TextView tvBLocation;
    TextView tvBStartDate;
    TextView tvBEndDate;

    TextView tvYourBookings;
    TextView tvWhenYouBook;
    TextView btnStartExploring;



    private int mHistoryPageUser;
    private ArrayList<Booking> bookingArrayList;
    private ArrayAdapter<Booking> bookingArrayAdapter;
    private ListView lvBookingsHistory;

    public FragmentCommunicator communicator;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (FragmentCommunicator) activity;
    }




    public static HistoryPageUserFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HistoryPageUserFragment fragment = new HistoryPageUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryPageUser = getArguments().getInt(ARG_PAGE);


    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_user_page, container, false);

        ivBCoverPicture = (ImageView) view.findViewById(R.id.ivBHCoverPicture);
        ivBProfilePicture = (ImageView) view.findViewById(R.id.ivBHProfilePicture);
        tvBTitle = (TextView) view.findViewById(R.id.tvBHTitle);
        tvBLocation = (TextView) view.findViewById(R.id.tvBHLocation);
        tvBStartDate = (TextView) view.findViewById(R.id.tvBHStartDate);
        tvBEndDate = (TextView) view.findViewById(R.id.tvBHEndDate);
        lvBookingsHistory = (ListView) view.findViewById(R.id.lvBookingsHistory);
        tvYourBookings = (TextView) view.findViewById(R.id.tvYourBookings);
        tvWhenYouBook = (TextView) view.findViewById(R.id.tvWhenYouBook);
        btnStartExploring = (Button) view.findViewById(R.id.btnStartExploring);


        bookingArrayList = new ArrayList<>();
        bookingArrayAdapter = new BookingsHistoryAdapter(getActivity(), bookingArrayList);
        lvBookingsHistory.setAdapter(bookingArrayAdapter);

        setUpClickListeners();


        getBookingHistory();


        return view;
    }


    public void setUpClickListeners()
    {
        btnStartExploring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Start Exploring", Toast.LENGTH_SHORT).show();
                communicator.startExploring();

            }
        });
    }


    public void getBookingHistory()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayBookingHistoryTable);
            query.whereEqualTo(Constants.ownerIdKey, ParseUser.getCurrentUser());
            query.include("listingId.sitterId");
            query.include("listingId.listing_pictures");
            query.orderByDescending(Constants.startDateKey);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> myBookings, ParseException e) {
                    if (e == null) {
                        bookingArrayList.addAll(Booking.fromParseObjectList(myBookings));
                        bookingArrayAdapter.notifyDataSetChanged();
                    } else {
                        // Log.e(TAG, "Error: " + e.getMessage());

                        Toast.makeText(getActivity(),getResources().getString(R.string.generic_error),Toast.LENGTH_SHORT).show();
                    }


                    if(myBookings.isEmpty())
                    {
                        tvYourBookings.setVisibility(View.VISIBLE);
                        tvWhenYouBook.setVisibility(View.VISIBLE);
                        btnStartExploring.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        tvYourBookings.setVisibility(View.INVISIBLE);
                        tvWhenYouBook.setVisibility(View.INVISIBLE);
                        btnStartExploring.setVisibility(View.INVISIBLE);

                    }

                }
            });

        }

    }

    @Override
    public void startExploring() {

    }

    @Override
    public void login() {

    }

    @Override
    public void listYourSpace() {

    }
}

