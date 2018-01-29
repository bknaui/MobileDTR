package com.example.asnaui.mobiledtr.Object;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrDate {
    public String date;
    public ArrayList<DtrTime> time;

    public DtrDate(String date, ArrayList<DtrTime> time) {
        this.date = date;
        this.time = time;
    }
}
