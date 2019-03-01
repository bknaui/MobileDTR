package com.example.asnaui.mobiledtr.contract;

import com.example.asnaui.mobiledtr.model.LeaveModel;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeaveContract {

    public interface LeaveView{
        void displayLeave();
    }
    public interface LeavePresenter{
        void addLeave(LeaveModel object);
    }
}
