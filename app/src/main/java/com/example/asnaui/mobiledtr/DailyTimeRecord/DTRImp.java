package com.example.asnaui.mobiledtr.DailyTimeRecord;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRImp {

    public interface DTRView{
        void displayList();
        void displayTimelogDialog(String status,String time);
    }
    public interface DTRPresenter{
        void addTimeLogs(String date,String time,String filePath);
    }
}
