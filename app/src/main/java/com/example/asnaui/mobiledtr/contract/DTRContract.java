package com.example.asnaui.mobiledtr.contract;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRContract {

    public interface DTRView{
        void displayList();
        void displayTimelogDialog(String status,String time);
    }
    public interface DTRPresenter{
        void addTimeLogs(String date,String time,String filePath,String latitude,String longitude);
    }
}
