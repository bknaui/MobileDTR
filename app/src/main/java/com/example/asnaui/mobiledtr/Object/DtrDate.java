package com.example.asnaui.mobiledtr.Object;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrDate {
    public String date, id;
    public ArrayList<DtrTime> list;

    public DtrDate(String id, String date, ArrayList<DtrTime> list) {
        this.id = id;
        this.date = date;
        this.list = list;
    }
}
