package com.example.asnaui.mobiledtr.presenter;

import com.example.asnaui.mobiledtr.contract.LeaveContract;
import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.model.LeaveModel;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeavePresenter implements LeaveContract.LeavePresenter {

    LeaveContract.LeaveView view;
    DBContext dbContext;

    public LeavePresenter(LeaveContract.LeaveView view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }


    @Override
    public void addLeave(LeaveModel object) {
        dbContext.insertLeave(object);
        view.displayLeave();
    }
}
