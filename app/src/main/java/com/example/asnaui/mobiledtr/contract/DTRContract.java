package com.example.asnaui.mobiledtr.contract;

import android.location.Location;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRContract {

    public interface DTRView {
        void displayList();

        void displayTimelogDialog(String status, String time);

        void displayErrorDialog();

        void updateLocationStatus(String location, int backgroundColor);
    }

    public interface DTRPresenter {
        void addTimeLogs(String date, String time, String filePath, String latitude, String longitude);
    }

    public interface LocationCallback {
        void setLocation(Location location);

        void updateStatus(String message, int color);
    }
}
