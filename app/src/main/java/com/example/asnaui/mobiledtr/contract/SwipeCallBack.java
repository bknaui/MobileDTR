package com.example.asnaui.mobiledtr.contract;

import com.example.asnaui.mobiledtr.model.LeaveModel;
import com.example.asnaui.mobiledtr.model.OfficeOrderModel;

public class SwipeCallBack {

    public interface OfficeOrder {
        void onItemDelete(OfficeOrderModel officeOrder);
    }

    public interface Leave {
        void onItemDelete(LeaveModel leaveModel);
    }

    public interface Cto {
        void onItemDelete(String value);
    }

}
