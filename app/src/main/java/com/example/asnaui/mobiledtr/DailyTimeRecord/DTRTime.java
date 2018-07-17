package com.example.asnaui.mobiledtr.DailyTimeRecord;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRTime {
    public String date,time, status,filePath;

    public DTRTime(String date, String time, String status,String filePath) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.filePath = filePath;
    }
}
