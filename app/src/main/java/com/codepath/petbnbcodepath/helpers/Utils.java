package com.codepath.petbnbcodepath.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.petbnbcodepath.activities.DetailsPageActivity;
import com.codepath.petbnbcodepath.models.Listing;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    static public LatLng getCurrentLatLng(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            return new LatLng(Constants.defaultLatitude, Constants.defaultLongitude);
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        return new LatLng(latitude, longitude);
    }


    static public String getCityName(Context context, double latitude, double longitude) {
        {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            String city = "";

            LatLng latLng = getCurrentLatLng(context);
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Sorry, Not able to get current city name, please type it.", Toast.LENGTH_SHORT).show();
            }
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String cityName[] = {"", "", ""};
                int idx = 0;
                if (address.getLocality() != null)
                    cityName[idx++] = address.getLocality();
                if (address.getAdminArea() != null)
                    cityName[idx++] = address.getAdminArea();
                if (address.getCountryCode() != null)
                    cityName[idx++] = address.getCountryCode();

                StringBuilder sb = new StringBuilder();
                for (String n : cityName) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(n);
                }
                return sb.toString();
            } else {
                Toast.makeText(context, "Sorry, Not able to get current city name, please type it.", Toast.LENGTH_SHORT).show();
            }
            return city;

        }

    }

    static public String getCurrentCityName(Context context) {
        LatLng latLng = getCurrentLatLng(context);
        return getCityName(context, latLng.latitude, latLng.longitude);
    }

    static public String getCurrentLocationString(Context context) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        String city = "";

        LatLng latLng = getCurrentLatLng(context);
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Sorry, Not able to get current city name, please type it.", Toast.LENGTH_SHORT).show();
        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(" ");
            }
            return sb.toString().trim();
        } else {
            Toast.makeText(context, "Sorry, Not able to get current location, please type it.", Toast.LENGTH_SHORT).show();
        }
        return city;

    }

    static public void hideKeyboard(Activity activity) {
        if (activity == null)
            return;
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                in.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void setTealBorder(ImageView view) {
        int color = Color.parseColor(Constants.TEAL_COLOR);
        view.setColorFilter(color);
    }


    public static void gotoDetailsPage(Context context, Listing currentListing) {
        gotoDetailsPage(context, currentListing, false);
    }

    public static void gotoDetailsPage(Context context, Listing currentListing, boolean isPreview) {
        Intent intent = new Intent(context, DetailsPageActivity.class);
        intent.putExtra(Constants.firstNameKey, currentListing.getFirst_name());
        intent.putExtra(Constants.lastNameKey, currentListing.getLast_name());
        intent.putExtra(Constants.coverPictureKey, currentListing.getCoverPictureUrl());
        intent.putExtra(Constants.titleKey, currentListing.getTitle());
        intent.putExtra(Constants.reviewerIdKey, currentListing.getNumReviews());
        intent.putExtra(Constants.firstReview, String.valueOf(currentListing.getFirstReview()));
        intent.putExtra(Constants.listingCostKey, currentListing.getCost());
        intent.putExtra(Constants.descriptionKey, currentListing.getDescription());
        intent.putExtra(Constants.hasPetsKey, currentListing.isHasPets());
        intent.putExtra(Constants.houseTypeKey, currentListing.getHomeType());
        intent.putExtra(Constants.petTypeKey, currentListing.getPetType());
        intent.putExtra(Constants.IS_PREVIEW, isPreview);
        intent.putExtra(Constants.objectIdKey, currentListing.getObjectId());
        intent.putStringArrayListExtra(Constants.IMAGE_URL_LIST,currentListing.getImageUrlList());


        context.startActivity(intent);
    }

    public static void gotoDetailsPage(Context context, Listing currentListing, boolean isPreview,ImageView ivProfile) {
        Intent intent = new Intent(context, DetailsPageActivity.class);
        intent.putExtra(Constants.firstNameKey, currentListing.getFirst_name());
        intent.putExtra(Constants.lastNameKey, currentListing.getLast_name());
        intent.putExtra(Constants.coverPictureKey, currentListing.getCoverPictureUrl());
        intent.putExtra(Constants.titleKey, currentListing.getTitle());
        intent.putExtra(Constants.reviewerIdKey, currentListing.getNumReviews());
        intent.putExtra(Constants.firstReview, String.valueOf(currentListing.getFirstReview()));
        intent.putExtra(Constants.listingCostKey, currentListing.getCost());
        intent.putExtra(Constants.descriptionKey, currentListing.getDescription());
        intent.putExtra(Constants.hasPetsKey, currentListing.isHasPets());
        intent.putExtra(Constants.houseTypeKey, currentListing.getHomeType());
        intent.putExtra(Constants.petTypeKey, currentListing.getPetType());
        intent.putExtra(Constants.IS_PREVIEW, isPreview);
        intent.putExtra(Constants.objectIdKey, currentListing.getObjectId());
        intent.putStringArrayListExtra(Constants.IMAGE_URL_LIST,currentListing.getImageUrlList());

        ActivityOptionsCompat options = ActivityOptionsCompat.
              makeSceneTransitionAnimation((Activity) context, (View) ivProfile, "profile");

        context.startActivity(intent,options.toBundle());
    }

    public static  byte[] readBytesFromURI(Context context,Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream;
        try {
             inputStream = context.getContentResolver().openInputStream(uri);

        }catch (FileNotFoundException e){
            String path = uri.getPath();
            File file = new File(path);
            Uri uri1 = Uri.fromFile(file);
            inputStream = context.getContentResolver().openInputStream(uri1);
        }
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
}
