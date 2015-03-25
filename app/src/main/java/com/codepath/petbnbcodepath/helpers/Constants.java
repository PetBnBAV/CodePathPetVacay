package com.codepath.petbnbcodepath.helpers;

import com.parse.ParseGeoPoint;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by vibhalaljani on 3/7/15.
 */
public class Constants {
    public static final String APPLICATION_ID = "yyCmYRd4b99Jihn7YhGIZYNlgb3NMWxeiIOL0CQw";
    public static final String CLIENT_KEY = "ZFCnvTC39XpM2J0DpwXgfEUmoaItS4UzOZ1OEcmv";
    public static final String petVacayUserTable = "_User";
    public static final String petVacayOwnerTable = "PetVacayOwner";
    public static final String petVacayListingTable = "PetVacayListing";
    public static final String petVacayReviewTable = "PetVacayReview";
    public static final String petVacayBookingHistoryTable = "PetVacayBookingHistory";

    public static final int whereWithinMiles = 20;
    public static final int nearbyQueryLimit = 10;
    public static final int zoom = 10;

    public static final String listingCostKey = "cost";
    public static final String listingLatlngKey = "latlng";
    public static final String sitterIdKey = "sitterId";
    public static final String firstNameKey = "first_name";
    public static final String lastNameKey = "last_name";
    public static final String coverPictureKey = "cover_picture";
    public static final String profilePictureKey = "profile_picture";
    public static final String listingIdKey = "listingId";
    public static final String reviewerIdKey = "reviewerId";
    public static final String objectIdKey = "objectId";

    public static final String ownerIdKey = "ownerId";
    public static final String summaryKey = "listingSummary";
    public static final String numReviewsKey = "numReviews";
    public static final String descriptionKey = "description";

    public static final String latitude = "latitude";
    public static final String longitude = "longitude";

    public static final String GOOGLE_API_KEY = "AIzaSyDWL29OaAFz5WyJ_e-bU3UmJ8QruGpFQxQ";

    public static final String currencySymbol = Currency.getInstance(Locale.US).getSymbol(Locale.US);

    public static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

    public static final float btnDisabledAlpha = 0.2f;
    public static final float btnEnabledAlpha = 1;

    public static final double service_charge_percentage = 0.15;

    public static ParseGeoPoint currLatLng;
    public static final String pushCustomDataKey = "userObjectId";

    public static final String firstReview = "firstReview";

    public static final String alertKey = "alert";
    public static final String userKey = "user";
    public static final String bookingIdKey = "bookingId";
    public static final String costPerNightKey = "cost_per_night";
    public static final String startDateKey = "startDate";
    public static final String endDateKey = "endDate";
    public static final String pendingKey = "pending";
    public static final String msgKey = "message";
    public static final String whoamiKey = "whoami";
    public static final String petOwnerKey = "petOwner";
    public static final String petSitterKey = "petSitter";
    public static final String titleKey = "title";
    public static final String hasPetsKey = "hasPets";
    public static final String petTypeKey= "petType";
    public static final String homeTypeKey= "homeType";
    public static final String latlngKey = "latlng";
    public static final String costKey = "cost";
    public static String locationStrKey = "locationStr";
    public static String listingPicturesKey = "listing_pictures";

    public enum PET_TYPE {
        DOG(0), CAT(1), BOTH(2);
        private int value;
        public int getValue(){
            return value;
        }
        private PET_TYPE(int value) {
            this.value = value;
        }
    };
    public static final String houseTypeKey = "house_type";
    public static final String cityKey = "city";
    public static final String petCountKey = "pet_count";
    public static final String petSizeKey = "pet_size";
    public static final String playgroundKey = "play_ground";
    public static final double defaultLatitude = 37.3541;
    public static final double defaultLongitude = -121.955;
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SECONADRY_ICON = "secondaryIconResource";
    public static final String TEAL_COLOR = "#008080";
    public static final String IS_PREVIEW="is_preview";

}
