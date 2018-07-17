package com.example.asnaui.mobiledtr.OfficeOrder;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class OfficeOrderImp {
    public interface View {
        void display();
    }
    public interface Presenter{
        void add(OfficeOrderItem object);
    }
}
