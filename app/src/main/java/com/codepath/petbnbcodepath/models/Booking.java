package com.codepath.petbnbcodepath.models;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anuscorps23 on 3/22/15.
 */
public class Booking {

    String bookingStartDate;
    String bookingEndDate;

    ParseObject listingId;
    ParseObject sitterId;
    ParseObject listing_pictures;


    ParseFile listingCoverPicture;
    ParseFile userProfilePicture;
    String listingTitle;

    ParseGeoPoint latlng;




    public ParseGeoPoint getLatlng() {
        return latlng;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public ParseObject getListingId() {
        return listingId;
    }

    public ParseObject getListing_pictures() {
        return listing_pictures;
    }

    public ParseObject getSitterId() {
        return sitterId;
    }


    public ParseFile getListingCoverPicture() {
        return listingCoverPicture;
    }


    public ParseFile getUserProfilePicture() {
        return userProfilePicture;
    }


    public String getListingTitle() {
        return listingTitle;
    }



    public static ArrayList<Booking> fromParseObjectList(List<ParseObject> bookingsList) {
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (int i = 0; i < bookingsList.size(); i++) {
            userBookings.add(Booking.fromParseObject(bookingsList.get(i)));
        }
        return userBookings;
    }


    public static Booking fromParseObject(ParseObject bookings) {

        final Booking bookingList = new Booking();

        SimpleDateFormat format = new SimpleDateFormat("MMM dd");

        Date startDate = bookings.getDate("startDate");
        Date endDate =  bookings.getDate("endDate");

        bookingList.bookingStartDate = format.format(startDate);
        bookingList.bookingEndDate = format.format(endDate);

        bookingList.listingId = bookings.getParseObject("listingId");
        bookingList.listing_pictures =  bookingList.listingId.getParseObject("listing_pictures");
        bookingList.sitterId = bookingList.listingId.getParseObject("sitterId");

        bookingList.listingTitle = bookingList.listingId.getString("title");
        bookingList.latlng = bookingList.listingId.getParseGeoPoint("latlng");


        bookingList.userProfilePicture = bookingList.sitterId.getParseFile("profile_picture");
        bookingList.listingCoverPicture = bookingList.listing_pictures.getParseFile("lst_picOne");


        return bookingList;
    }


}
