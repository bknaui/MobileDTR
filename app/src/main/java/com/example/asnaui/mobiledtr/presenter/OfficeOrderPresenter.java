package com.example.asnaui.mobiledtr.presenter;

import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.contract.OfficeOrderContract;
import com.example.asnaui.mobiledtr.model.OfficeOrderModel;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class OfficeOrderPresenter implements OfficeOrderContract.Presenter {

    OfficeOrderContract.View view;
    DBContext dbContext;

    public OfficeOrderPresenter(OfficeOrderContract.View view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }

    @Override
    public void add(OfficeOrderModel object) {
            dbContext.insertSO(object);
            view.display();
    }
}
