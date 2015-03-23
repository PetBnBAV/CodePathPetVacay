package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.adapters.PlacesAutoCompleteAdapter;
import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.helpers.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by gangwal on 3/14/15.
 */
public class LYSCityFragment extends Fragment {

    private static Activity sActivity;
    private CitySelectListner mCallback;
    private AutoCompleteTextView etSearch;
    private Button tvStickyButton;
    private ImageView ivCurrentLocation;
    private boolean stickyButtonEnabled = false;
    String query ="";

    private GoogleMap map;
    private SupportMapFragment mapFragment;

    public static LYSCityFragment getInstance(Activity activity){
        LYSCityFragment frag = new LYSCityFragment();
        sActivity = activity;
        return frag;
    }

    public interface CitySelectListner {
        public void getCityName(String city);
        public void setToolbar(String title, String secondaryTitle);
    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (CitySelectListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CitySelectListner");
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

        final LatLng currentLocation = Utils.getCurrentLatLng(getActivity());
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_lsy_city,parent,false);
        setupView(view);
        setupViewListeners();
        return view;
    }

    public void setupView(View view){
        mCallback.setToolbar(getActivity().getResources().getString(R.string.city),"");

        etSearch = (AutoCompleteTextView) view.findViewById(R.id.etSearch);
        etSearch.setAdapter(new PlacesAutoCompleteAdapter(sActivity, R.layout.list_item));
        tvStickyButton = (Button) view.findViewById(R.id.btNext);

        tvStickyButton.setEnabled(false);
        tvStickyButton.setAlpha(Constants.btnDisabledAlpha);
        ivCurrentLocation = (ImageView) view.findViewById(R.id.ivCurrentLocation);
        int color = Color.parseColor(Constants.TEAL_COLOR);
        ivCurrentLocation.setColorFilter(color);


    }

    public void setupViewListeners() {
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query = (String) parent.getItemAtPosition(position);
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    stickyButtonEnabled = true;
                    tvStickyButton.setAlpha(Constants.btnEnabledAlpha);
                }else{
                    stickyButtonEnabled = false;
                    tvStickyButton.setAlpha(Constants.btnDisabledAlpha);
                }
                tvStickyButton.setEnabled(stickyButtonEnabled);

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = Utils.getCurrentCityName(getActivity());
                etSearch.setText("");
                etSearch.setText(city);
            }
        });

        tvStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!stickyButtonEnabled)
                    return;
                mCallback.getCityName(query);
            }
        });
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
//            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();

            /*map.setMyLocationEnabled(true);
            map.setOnMapLongClickListener(this);

            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            connectClient();*/
        } else {
//            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }
}
