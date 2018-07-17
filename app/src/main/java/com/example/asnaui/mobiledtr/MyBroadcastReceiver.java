package com.example.asnaui.mobiledtr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;

/**
 * Created by apangcatan on 26/04/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean anyLocationProv = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.e("SETTINGS", "Location service status" + anyLocationProv);
        if (context instanceof Home) {
            Home.mLocation.setVisibility(View.VISIBLE);
            if (!anyLocationProv) {
                Home.current = null;
                Home.mLocation.setText("GPS not enbaled");
            } else {
                Home.mLocation.setText("Location Callibrating... ");
            }
        }
    }
}
