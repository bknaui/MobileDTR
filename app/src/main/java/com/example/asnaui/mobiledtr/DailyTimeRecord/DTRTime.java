package com.example.asnaui.mobiledtr.DailyTimeRecord;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRTime implements Parcelable{
    public String date,time, status,filePath;

    public DTRTime(String date, String time, String status,String filePath) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.filePath = filePath;
    }

    protected DTRTime(Parcel in) {
        date = in.readString();
        time = in.readString();
        status = in.readString();
        filePath = in.readString();
    }

    public static final Creator<DTRTime> CREATOR = new Creator<DTRTime>() {
        @Override
        public DTRTime createFromParcel(Parcel in) {
            return new DTRTime(in);
        }

        @Override
        public DTRTime[] newArray(int size) {
            return new DTRTime[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(status);
        dest.writeString(filePath);
    }
}
