package com.example.asnaui.mobiledtr.DailyTimeRecord;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRDate implements Parcelable {
    public String date, id;
    public ArrayList<DTRTime> list;

    public DTRDate(String id, String date, ArrayList<DTRTime> list) {
        this.id = id;
        this.date = date;
        this.list = list;
    }

    protected DTRDate(Parcel in) {
        date = in.readString();
        id = in.readString();
        list = in.createTypedArrayList(DTRTime.CREATOR);
    }

    public static final Creator<DTRDate> CREATOR = new Creator<DTRDate>() {
        @Override
        public DTRDate createFromParcel(Parcel in) {
            return new DTRDate(in);
        }

        @Override
        public DTRDate[] newArray(int size) {
            return new DTRDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(id);
        dest.writeTypedList(list);
    }
}
