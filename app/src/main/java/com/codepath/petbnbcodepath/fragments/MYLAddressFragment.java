package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PlacesAutoCompleteAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by gangwal on 3/14/15.
 */
public class MYLAddressFragment extends Fragment {

    private AddressListingListner mCallback;
    private AutoCompleteTextView tvAddress;
    private TextView tvStickyButton;
    private ImageView ivCurrentLocation;
    private static String sAddress;

    private boolean stickyButtonEnabled = false;
    private float ALPHA_BUTTON_DISABLED = (float) 0.4;
    private float ALPHA_BUTTON_ENABLED = (float) 1.0;
    String query ="";


    private GoogleMap map;
    private SupportMapFragment mapFragment;

    public static MYLAddressFragment getInstance(Activity activity, String address){
        MYLAddressFragment frag = new MYLAddressFragment();
        sAddress = address;
        return frag;
    }

    public interface AddressListingListner {
        public void addressListing(String value);
    }


    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (AddressListingListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddressListingListner");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }

        final double latitude = 37.3541;//TODO need to get it from city name or current location.
        final double longitude = -121.955;
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude)
                            , Constants.zoom));
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
/***at this time google play services are not initialize so get map and add what ever you want to it in onResume() or onStart() **/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = mapFragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_mly_address,parent,false);
        setUpViews(view);
        setupListners(view);
        return view;

    }

    private void setupListners(View view) {
        //TODO Once Done button is there,make this as dialog
        tvStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.addressListing(String.valueOf(tvAddress.getText()));
            }
        });

        {
            tvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    query = (String) parent.getItemAtPosition(position);
                }
            });

            tvAddress.addTextChangedListener(new TextWatcher(){
                public void afterTextChanged(Editable s) {
                    if(s.length()>0){
                        tvStickyButton.setAlpha(ALPHA_BUTTON_ENABLED);
                        stickyButtonEnabled = true;
                    }else{
                        tvStickyButton.setAlpha(ALPHA_BUTTON_DISABLED);
                        stickyButtonEnabled = false;
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count){}
            });

            tvStickyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!stickyButtonEnabled)
                        return;
                    mCallback.addressListing(query);
                }
            });
        }
    }

    private void setUpViews(View view) {
        tvAddress = (AutoCompleteTextView)view.findViewById(R.id.etSearch);
        tvAddress.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));

        tvStickyButton = (TextView)view.findViewById(R.id.tvNext);
        ivCurrentLocation = (ImageView)view.findViewById(R.id.ivCurrentLocation);
    }
}
