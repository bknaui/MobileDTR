package com.example.asnaui.mobiledtr.Global;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTR;

import java.io.File;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class Constant {
    public static String base_url = "http://210.4.59.2";

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        return mdformat.format(calendar.getTime());
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        return mdformat.format(calendar.getTime());
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), "auto_time", 0) == 1;
        }
    }

    public static void deletePictures() {
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), ".MobileDTR/MyImages");
        if (imagesFolder.isDirectory()) {
            String[] children = imagesFolder.list();
            for (int i = 0; i < children.length; i++) {
                new File(imagesFolder, children[i]).delete();
            }
        }
    }

    public static double isWithinRadius(Location test) {
        Location center = new Location("CENTER");
        center.setLatitude(10.3076739);
        center.setLongitude(123.8931655);
        return test.distanceTo(center);
    }

    public static double meterDistanceBetweenPoints(float lat_a, float lng_a) {
        float pk = (float) (180.f / Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = (float) (10.3076739 / pk);
        float b2 = (float) (123.8931655 / pk);

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public static String getMacAddr() {

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }

        return "FAIL";
    }

    public static int getPosition(String date) {
        int i = 0;
        for (; i < DTR.list.size(); i++) {
            if (DTR.list.get(i).date.equalsIgnoreCase(date)) break;
        }
        return i;
    }
}
