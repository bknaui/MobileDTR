package com.example.asnaui.mobiledtr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;

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
        if (!anyLocationProv) {
            MainActivity.current = null;
            MainActivity.scan.setVisibility(View.GONE);
            Toast.makeText(context, "Location is not updated, Location settings must be enabled", Toast.LENGTH_SHORT).show();
        }

    }
}
