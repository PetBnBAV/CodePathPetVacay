package com.codepath.petbnbcodepath.helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by gangwal on 3/20/15.
 */
public class Utils {

    static public ParseGeoPoint getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        ParseGeoPoint p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    static public LatLng getCurrentLatLng(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null){
            return new LatLng(Constants.defaultLatitude,Constants.defaultLongitude);
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        return new LatLng(latitude,longitude);
    }

    static public String getCurrentCityName(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        String city= "";

        LatLng latLng = getCurrentLatLng(context);
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Sorry, Not able to get current city name, please type it.",Toast.LENGTH_SHORT).show();
        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            String cityName[]={"","",""};
            int idx=0;
            if(address.getLocality()!=null)
                cityName[idx++] = address.getLocality();
            if(address.getAdminArea()!=null)
                cityName[idx++] = address.getAdminArea();
            if(address.getCountryCode()!=null)
                cityName[idx++] = address.getCountryCode();

            StringBuilder sb = new StringBuilder();
            for (String n : cityName) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(n);
            }
            return sb.toString();
        }
        else {
            Toast.makeText(context, "Sorry, Not able to get current city name, please type it.", Toast.LENGTH_SHORT).show();
        }
        return city;

    }

    static public String getCurrentLocationString(Context context){
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        String city= "";

        LatLng latLng = getCurrentLatLng(context);
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Sorry, Not able to get current city name, please type it.",Toast.LENGTH_SHORT).show();
        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(" ");
            }
            return sb.toString().trim();
        }
        else {
            Toast.makeText(context, "Sorry, Not able to get current city name, please type it.", Toast.LENGTH_SHORT).show();
        }
        return city;

    }

    static public void hideKeyboard(Activity activity){
        if(activity==null)
            return;
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(in!=null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                in.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);}
        }
    }
}
