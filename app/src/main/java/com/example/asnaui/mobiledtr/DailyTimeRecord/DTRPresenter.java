package com.example.asnaui.mobiledtr.DailyTimeRecord;

import android.util.Log;

import com.example.asnaui.mobiledtr.Global.DBContext;

import java.util.ArrayList;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class DTRPresenter implements DTRImp.DTRPresenter {

    DTRImp.DTRView view;
    DBContext dbContext;
    String id = "";

    public DTRPresenter(DTRImp.DTRView view, DBContext dbContext, String id) {
        this.view = view;
        this.dbContext = dbContext;
        this.id = id;
    }

    @Override
    public void addTimeLogs(String date, String time, String filePath) {
        Log.e("AddTime", date + " " + time + " ");
        ArrayList<DTRTime> mList = new ArrayList<>();
        if (!dbContext.dateExists(date)) {
            dbContext.insertDate(new DTRDate(id, date, null));
            DTR.list.add(new DTRDate(id, date, mList));
        }
        String status = dbContext.lastStatus(date);
        DTRTime timelog = new DTRTime(date, time, status, filePath);
//
//        mList.add(timelog);
//        DTR.list.get(Constant.getPosition(date)).list = mList;
        dbContext.insertLogs(timelog);

        view.displayTimelogDialog(timelog.status, timelog.time);
        // DTR.list.add(new DTRDate(id, date, DTR.list.get(Constant.getPosition(DTR.list, date)).list));
        view.displayList();
    }
}
