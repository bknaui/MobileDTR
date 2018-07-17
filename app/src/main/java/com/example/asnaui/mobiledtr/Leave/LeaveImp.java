package com.example.asnaui.mobiledtr.Leave;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeaveImp {

    interface LeaveView{
        void displayLeave();
    }
    interface LeavePresenter{
        void addLeave(LeaveItem object);
    }
}
