package com.example.asnaui.mobiledtr.DailyTimeRecord;

import com.example.asnaui.mobiledtr.Global.Constant;
import com.example.asnaui.mobiledtr.Global.DBContext;

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
    public void addTimeLogs() {
        if(!dbContext.dateExists(Constant.getCurrentDate())){
            dbContext.insertDate(new DTRDate(id,Constant.getCurrentDate(),null));
        }
        dbContext.insertLogs(new DTRTime(Constant.getCurrentDate(),Constant.getCurrentTime(),dbContext.lastStatus(Constant.getCurrentDate())));
        view.displayList();
    }
}
