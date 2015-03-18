package com.codepath.petbnbcodepath.net;

import android.content.Context;
import android.content.Intent;

import com.codepath.petbnbcodepath.activities.PetOwnerProfileActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by vibhalaljani on 3/18/15.
 */
public class PetVacayBroadcastReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected Class<PetOwnerProfileActivity> getActivity(Context context, Intent intent) {
        return PetOwnerProfileActivity.class;
    }
}
