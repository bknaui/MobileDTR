package com.example.asnaui.mobiledtr.CompensatoryTimeOff;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOffImp {
    public interface View {
        void display();
    }
    public interface Presenter{
        void add(String date);
    }
}
