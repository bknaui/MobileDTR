package com.example.asnaui.mobiledtr.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class Constant {
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy");
        return mdformat.format(calendar.getTime());
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        return mdformat.format(calendar.getTime());
    }
}
