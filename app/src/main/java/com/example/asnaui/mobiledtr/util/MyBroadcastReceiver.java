package com.example.asnaui.mobiledtr.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.contract.DTRContract;

/**
 * Created by apangcatan on 26/04/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    DTRContract.LocationCallback callback;

    public MyBroadcastReceiver(DTRContract.LocationCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!LocationManagerHelper.isLocationEnabled(context)) {
            callback.setLocation(null);
            callback.updateStatus("Location/GPS not enabled", ContextCompat.getColor(context, R.color.gps_disabled));

        } else {
            callback.updateStatus("Location/GPS Callibrating...", ContextCompat.getColor(context,R.color.callibrating));
        }
    }
}
