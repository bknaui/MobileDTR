package com.example.asnaui.mobiledtr.DailyTimeRecord;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRDate {
    public String date, id;
    public ArrayList<DTRTime> list;

    public DTRDate(String id, String date, ArrayList<DTRTime> list) {
        this.id = id;
        this.date = date;
        this.list = list;
    }
}
