package com.codepath.petbnbcodepath.models;

import com.codepath.petbnbcodepath.helpers.Constants;
import com.codepath.petbnbcodepath.net.GoogleMapReverseGeoCodingClient;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibhalaljani on 3/8/15.
 *

 * This is a model class for our listing schema. It is used for the landing page and the
 * swiping fragments below the map.
 */
public class Listing {

    private static final String TAG = "LISTING";

    // Stores the actual latitude, longitude because that's what we pass around
    private ParseGeoPoint latLng;

    private String title;
    // Stores the result of the reverse geocoding - currently just the city and state

    private String cityState;
    private String first_name;
    private String last_name;
    private String coverPictureUrl;

    public String getDescription() {
        return description;
    }

    private String description;
    private String objectId;

    private int cost;
    private int numReviews;

    public int getHomeType() {
        return homeType;
    }

    public int getPetType() {
        return petType;
    }

    public boolean isHasPets() {
        return hasPets;
    }

    private int homeType;
    private int petType;
    private boolean hasPets;

    public ArrayList<ParseFile> getListingPictureList() {
        return listingPictureList;
    }

    public void setListingPictureList(ArrayList<ParseFile> listingPictureList) {
        this.listingPictureList = listingPictureList;
    }

    public int getListingPictureCount() {
        return listingPictureCount;
    }

    public void setListingPictureCount(int listingPictureCount) {
        this.listingPictureCount = listingPictureCount;
    }

    private ArrayList<ParseFile> listingPictureList;
    private int listingPictureCount;

    private Review firstReview;

    private GoogleMapReverseGeoCodingClient client;

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle(){
        return title;
    }

    public Review getFirstReview() {
        return firstReview;
    }

    public String getCityState() {
        return cityState;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public int getCost() {
        return cost;
    }

    public ParseGeoPoint getLatLng() {
        return latLng;
    }

    public static ArrayList<Listing> fromParseObjectList (List<ParseObject> listingList) {
        ArrayList<Listing> nearbyListings = new ArrayList();
        for (int i = 0; i < listingList.size(); i++) {
            nearbyListings.add(Listing.fromParseObject(listingList.get(i)));
        }
        return nearbyListings;
    }

    public static Listing fromParseObject(ParseObject listing) {
        final Listing currListing = new Listing();

        /*ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.petVacayReviewTable);
        query.whereEqualTo(Constants.listingIdKey, listing);
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    currListing.numReviews = count;
                } else {
                    Log.i(TAG, "Error: " + e.getMessage());
                }
            }
        });*/

        currListing.latLng = listing.getParseGeoPoint(Constants.listingLatlngKey);
        currListing.description = listing.getString(Constants.descriptionKey);
        currListing.cost = listing.getInt(Constants.listingCostKey);
        currListing.homeType = listing.getInt(Constants.homeTypeKey);
        currListing.petType = listing.getInt(Constants.petTypeKey);
        currListing.hasPets = listing.getBoolean(Constants.hasPetsKey);
        currListing.title = listing.getString(Constants.titleKey);

        /*currListing.client = new GoogleMapReverseGeoCodingClient();
        currListing.client.getCity(currListing.latLng);*/
        currListing.objectId = listing.getObjectId();

        // listener pattern because the reverse geocoding call that returns the city is an
        // asynchronous call. So we set the city only when a value is returned
        /*currListing.client.setCityListener(new GoogleMapReverseGeoCodingClient.GeoCodingListener() {
            @Override
            public void onCityLoaded(String city) {
                currListing.cityState = city;

            }
        });*/

        ParseObject sitter = listing.getParseObject(Constants.sitterIdKey);
        currListing.first_name = sitter.getString(Constants.firstNameKey);
        currListing.last_name = sitter.getString(Constants.lastNameKey);
        ParseFile coverPictureFile = sitter.getParseFile("profile_picture");
        if (coverPictureFile != null) {
            currListing.coverPictureUrl = sitter.getParseFile("profile_picture").getUrl();
        }
        ParseObject listingPictures = listing.getParseObject(Constants.listingPicturesKey);

        currListing.listingPictureList = new ArrayList<ParseFile>();
        String []listingPictureItemKeys = {"lst_picOne","lst_picTwo","lst_picThree","lst_picFour","lst_picFive"};
        for(String listingPictureItem : listingPictureItemKeys)
        {
            ParseFile listingPictureFile = null;
            try {
                listingPictureFile = listingPictures.fetchIfNeeded().getParseFile(listingPictureItem);
            } catch (ParseException e) {
                listingPictureFile =null;
                e.printStackTrace();
            }
            if(listingPictureFile!=null){
                currListing.listingPictureList.add(listingPictureFile);
            }
        }
         currListing.listingPictureCount = currListing.listingPictureList.size();

        return currListing;
    }

    public void setLatLng(ParseGeoPoint latLng) {
        this.latLng = latLng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCoverPictureUrl(String coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setHomeType(int homeType) {
        this.homeType = homeType;
    }

    public void setPetType(int petType) {
        this.petType = petType;
    }

    public void setHasPets(boolean hasPets) {
        this.hasPets = hasPets;
    }
}
