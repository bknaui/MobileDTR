package com.example.asnaui.mobiledtr.OfficeOrder;

import com.example.asnaui.mobiledtr.Global.DBContext;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class OfficeOrderPresenter implements OfficeOrderImp.Presenter {

    OfficeOrderImp.View view;
    DBContext dbContext;

    public OfficeOrderPresenter(OfficeOrderImp.View view, DBContext dbContext) {
        this.view = view;
        this.dbContext = dbContext;
    }

    @Override
    public void add(OfficeOrderItem object) {
            dbContext.insertSO(object);
            view.display();
    }
}
