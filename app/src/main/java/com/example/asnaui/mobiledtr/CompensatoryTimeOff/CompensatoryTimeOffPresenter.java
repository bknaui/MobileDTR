package com.example.asnaui.mobiledtr.CompensatoryTimeOff;

import com.example.asnaui.mobiledtr.Global.DBContext;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOffPresenter implements CompensatoryTimeOffImp.Presenter {

    CompensatoryTimeOffImp.View view;
    DBContext dbContext;

    public CompensatoryTimeOffPresenter(CompensatoryTimeOffImp.View view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }

    @Override
    public void add(String date) {
        dbContext.insertCTO(date);
        view.display();
    }
}
