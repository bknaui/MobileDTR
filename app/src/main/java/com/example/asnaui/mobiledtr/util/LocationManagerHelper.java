package com.example.asnaui.mobiledtr.util;

import android.content.Context;
import android.location.LocationManager;

public class LocationManagerHelper {


    public static boolean isLocationEnabled(Context context) {
        boolean anyLocationProv = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return anyLocationProv;
    }

}
