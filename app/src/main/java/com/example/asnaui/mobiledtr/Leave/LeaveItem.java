package com.example.asnaui.mobiledtr.Leave;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class LeaveItem {
    public String inclusive_date, leave_type;

    public LeaveItem(String leave_type, String inclusive_date) {
        this.inclusive_date = inclusive_date;
        this.leave_type = leave_type;
    }
}
