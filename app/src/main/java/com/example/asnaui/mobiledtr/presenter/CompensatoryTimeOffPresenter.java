package com.example.asnaui.mobiledtr.presenter;

import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.contract.CompensatoryTimeOffContract;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOffPresenter implements CompensatoryTimeOffContract.Presenter {

    CompensatoryTimeOffContract.View view;
    DBContext dbContext;

    public CompensatoryTimeOffPresenter(CompensatoryTimeOffContract.View view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }

    @Override
    public void add(String date) {
        dbContext.insertCTO(date);
        view.display();
    }
}
