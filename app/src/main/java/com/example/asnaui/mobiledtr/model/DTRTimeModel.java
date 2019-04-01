package com.example.asnaui.mobiledtr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DTRTimeModel implements Parcelable {
    public String date, time, status, filePath, latitude, longitude;

    public DTRTimeModel(String date, String time, String status, String filePath, String latitude, String longitude) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.filePath = filePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected DTRTimeModel(Parcel in) {
        date = in.readString();
        time = in.readString();
        status = in.readString();
        filePath = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public String formatToAmPm() {
        String[] time = this.time.split(":");
        int hour = Integer.parseInt(time[0]);
        String am_pm = "AM";
        if (hour > 12) {
            am_pm = "PM";
            return (hour - 12) + ":" + time[1] + ":" + time[2] + " " + am_pm;
        }
        if (hour == 12) {
            am_pm = "PM";
            return hour + ":" + time[1] + ":" + time[2] + " " + am_pm;
        }
        return hour + ":" + time[1] + ":" + time[2] + " " + am_pm;
    }

    public static final Creator<DTRTimeModel> CREATOR = new Creator<DTRTimeModel>() {
        @Override
        public DTRTimeModel createFromParcel(Parcel in) {
            return new DTRTimeModel(in);
        }

        @Override
        public DTRTimeModel[] newArray(int size) {
            return new DTRTimeModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

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
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
