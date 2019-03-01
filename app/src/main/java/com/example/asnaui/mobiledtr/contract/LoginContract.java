package com.example.asnaui.mobiledtr.contract;

import com.example.asnaui.mobiledtr.model.UserModel;
import com.example.asnaui.mobiledtr.network.JsonApi;

public class LoginContract {
    public interface LoginView{
        void onSuccess(UserModel userModel);

        void onFail(String message);

        JsonApi getApiInstance();
    }

    public interface LoginPresenter{
        void login(String url,String imei);
    }

    public interface LoginCallback{

        void onSuccess(UserModel userModel);

        void onFail(String message);
    }
}
