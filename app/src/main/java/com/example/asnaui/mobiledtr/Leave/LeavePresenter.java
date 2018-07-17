package com.example.asnaui.mobiledtr.Leave;

import com.example.asnaui.mobiledtr.Global.DBContext;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeavePresenter implements LeaveImp.LeavePresenter {

    LeaveImp.LeaveView view;
    DBContext dbContext;

    public LeavePresenter(LeaveImp.LeaveView view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }


    @Override
    public void addLeave(LeaveItem object) {
        dbContext.insertLeave(object);
        view.displayLeave();
    }
}
