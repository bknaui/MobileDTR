package com.example.asnaui.mobiledtr.DailyTimeRecord;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRImp {

    public interface DTRView{
        void displayList();
    }
    public interface DTRPresenter{
        void addTimeLogs();
    }
}
