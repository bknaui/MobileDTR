package com.example.asnaui.mobiledtr.presenter;

import android.util.Log;

import com.example.asnaui.mobiledtr.model.UserModel;
import com.example.asnaui.mobiledtr.contract.LoginContract;

public class LoginPresenter implements LoginContract.LoginPresenter {
    LoginContract.LoginView loginView;

    public LoginPresenter(LoginContract.LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(String url, String imei) {
        Log.e("Login", url + " " + imei);
        loginView.getApiInstance().Login(url, imei, new LoginContract.LoginCallback() {
            @Override
            public void onSuccess(UserModel userModel) {
                loginView.onSuccess(userModel);
            }

            @Override
            public void onFail(String message) {
                loginView.onFail(message);
            }
        });
    }
}
