package com.example.asnaui.mobiledtr.contract;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOffContract {
    public interface View {
        void display();
    }
    public interface Presenter{
        void add(String date);
    }
}
