package com.example.asnaui.mobiledtr.contract;

import com.example.asnaui.mobiledtr.model.OfficeOrderModel;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class OfficeOrderContract {
    public interface View {
        void display();
    }
    public interface Presenter{
        void add(OfficeOrderModel object);
    }
}
