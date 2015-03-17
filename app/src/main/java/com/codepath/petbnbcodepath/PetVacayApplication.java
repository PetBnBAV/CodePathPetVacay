package com.codepath.petbnbcodepath;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by anuscorps23 on 3/15/15.
 */
public class PetVacayApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "yyCmYRd4b99Jihn7YhGIZYNlgb3NMWxeiIOL0CQw";
    public static final String YOUR_CLIENT_KEY = "ZFCnvTC39XpM2J0DpwXgfEUmoaItS4UzOZ1OEcmv";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}