package com.codepath.petbnbcodepath;

import android.app.Application;

import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseUser;



/**
 * Created by anuscorps23 on 3/15/15.
 */
public class PetVacayApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Crash Reporting
        ParseCrashReporting.enable(this);

        Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_KEY);

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        if (ParseUser.getCurrentUser() != null) {
            installation.put("user", ParseUser.getCurrentUser());
        }

        installation.saveInBackground();


    }
}