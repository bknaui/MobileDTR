package com.example.asnaui.mobiledtr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRDateModel implements Parcelable {
    public String date, id;
    public ArrayList<DTRTimeModel> list;

    public DTRDateModel(String id, String date, ArrayList<DTRTimeModel> list) {
        this.date = date;
        this.id = id;
        this.list = list;
    }

    protected DTRDateModel(Parcel in) {
        date = in.readString();
        id = in.readString();
        list = in.createTypedArrayList(DTRTimeModel.CREATOR);
    }

    public static final Creator<DTRDateModel> CREATOR = new Creator<DTRDateModel>() {
        @Override
        public DTRDateModel createFromParcel(Parcel in) {
            return new DTRDateModel(in);
        }

        @Override
        public DTRDateModel[] newArray(int size) {
            return new DTRDateModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<DTRTimeModel> getList() {
        return list;
    }

    public void setList(ArrayList<DTRTimeModel> list) {
        this.list = list;
    }

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
