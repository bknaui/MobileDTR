package com.example.asnaui.mobiledtr.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.view.activity.Home;

/**
 * Created by apangcatan on 26/04/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!LocationManagerHelper.isLocationEnabled(context)) {
            Home.setCurrentLocation(null);
            Home.updateLocationStatus("GPS not enabled",ContextCompat.getColor(context,R.color.gps_disabled));
        } else {
            Home.updateLocationStatus("Location Callibrating...", ContextCompat.getColor(context,R.color.callibrating));
        }
    }
}
