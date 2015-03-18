package com.codepath.petbnbcodepath;

import android.app.Application;
import android.util.Log;

import com.codepath.petbnbcodepath.helpers.Constants;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by anuscorps23 on 3/15/15.
 */
public class PetVacayApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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