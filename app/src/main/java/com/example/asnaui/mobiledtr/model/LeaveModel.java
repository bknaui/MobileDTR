package com.example.asnaui.mobiledtr.model;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeaveModel {
    public String inclusive_date, leave_type;

    public LeaveModel(String leave_type, String inclusive_date) {
        this.inclusive_date = inclusive_date;
        this.leave_type = leave_type;
    }
}
