package com.example.asnaui.mobiledtr.presenter;

import android.util.Log;

import com.example.asnaui.mobiledtr.model.DTRTimeModel;
import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.contract.DTRContract;
import com.example.asnaui.mobiledtr.model.DTRDateModel;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRPresenter implements DTRContract.DTRPresenter {

    DTRContract.DTRView view;
    DBContext dbContext;
    String id = "";

    public DTRPresenter(DTRContract.DTRView view, DBContext dbContext, String id) {
        this.view = view;
        this.dbContext = dbContext;
        this.id = id;
    }

    @Override
    public void addTimeLogs(String date, String time, String filePath, String latitude, String longitude) {
        Log.e("AddTime", date + " " + time + " ");
        if (!dbContext.dateExists(date)) {
            dbContext.insertDate(new DTRDateModel(id, date, null));
        }

        String status = dbContext.lastStatus(date);
        DTRTimeModel timelog = new DTRTimeModel(date, time, status, filePath,latitude,longitude);
        dbContext.insertLogs(timelog);
        view.displayTimelogDialog(timelog.status, timelog.time);
        view.displayList();
    }
}
