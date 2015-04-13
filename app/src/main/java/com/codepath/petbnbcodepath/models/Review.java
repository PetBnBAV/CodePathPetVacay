package com.codepath.petbnbcodepath.models;

import android.nfc.Tag;
import android.util.Log;

import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vibhalaljani on 3/7/15.
 */
public class Review {
    private static String TAG = Review.class.getCanonicalName();
    private String reviewer_first_name;
    private String reviewer_city;
    private String reviewer_state;
    private String review_description;

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    private Date reviewDate;

    public String getReviewer_cover_picture() {
        return reviewer_cover_picture;
    }

    public void setReviewer_cover_picture(String reviewer_cover_picture) {
        this.reviewer_cover_picture = reviewer_cover_picture;
    }

    private String reviewer_cover_picture;

    public int getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(int review_rating) {
        this.review_rating = review_rating;
    }

    private int review_rating;

    public Review() {
        review_description = null;
        reviewer_city = null;
        reviewer_state = null;
        reviewer_first_name = null;
//        review_rating
    }

    public String getReviewer_first_name() {
        return reviewer_first_name;
    }

    public void setReviewer_first_name(String reviewer_first_name) {
        this.reviewer_first_name = reviewer_first_name;
    }

    public String getReviewer_city() {
        return reviewer_city;
    }

    public void setReviewer_city(String reviewer_city) {
        this.reviewer_city = reviewer_city;
    }

    public String getReviewer_state() {
        return reviewer_state;
    }

    public void setReviewer_state(String reviewer_state) {
        this.reviewer_state = reviewer_state;
    }

    public String getReview_description() {
        return review_description;
    }

    public void setReview_description(String review_description) {
        this.review_description = review_description;
    }


    public static Review fromParseObject(final ParseObject review) {
        final Review currReview = new Review();

        currReview.review_description = review.getString(Constants.descriptionKey);
        //reviewer ID
        currReview.review_rating = review.getInt(Constants.ratingKey);
        currReview.reviewDate = review.getCreatedAt();
        ParseObject reviewer = review.getParseObject(Constants.reviewerIdKey);
        try {
            currReview.reviewer_first_name = reviewer.fetchIfNeeded().getString(Constants.firstNameKey);
            currReview.reviewer_cover_picture = reviewer.fetchIfNeeded().getParseFile("profile_picture").getUrl();
        } catch (ParseException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
//        ParseFile coverPictureFile = sitter.getParseFile("profile_picture");
//        if (coverPictureFile != null) {
//            currListing.coverPictureUrl = sitter.getParseFile("profile_picture").getUrl();
//        }
        return currReview;
    }
}
