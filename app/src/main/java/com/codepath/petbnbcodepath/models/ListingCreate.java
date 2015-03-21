package com.codepath.petbnbcodepath.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

/**
 * Created by gangwal on 3/20/15.
 * This is primarily used to hold data while creating a list.
 * Most probably needs to be merged with Listing.java
 */
@AutoParcel
public abstract class ListingCreate implements Parcelable {
    public abstract int petType();
    public abstract int houseType();
    public abstract String city();
    public abstract int petCount();
    public abstract int petSize();
    public abstract int playground();
    public abstract String[] coverImages();
    public abstract String title();
    public abstract String summary();
    public abstract int cost();
    public abstract String address(); //Might need to have a object for this in future.

//    static ListingCreate create(int petType, int houseType, String city, int petCount, int petSize, int playground, String[] coverImages, String title, String summary, int cost, String address){
//        return new AutoParcel_ListingCreate(petType, houseType, city, petCount, petSize, playground, coverImages, title, summary, cost, address);
//    }
}
