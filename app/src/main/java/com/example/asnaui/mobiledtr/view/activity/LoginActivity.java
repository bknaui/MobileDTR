package com.example.asnaui.mobiledtr.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.contract.LoginContract;
import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.model.UserModel;
import com.example.asnaui.mobiledtr.network.JsonApi;
import com.example.asnaui.mobiledtr.presenter.LoginPresenter;
import com.example.asnaui.mobiledtr.util.Constant;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {

    private boolean isEnabled = false;
    private String IMEI = "";

    private Dialog dialog;
    private DBContext dbContext;
    private ProgressDialog pd;
    private TelephonyManager telephonyManager;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbContext = new DBContext(this);
        loginPresenter = new LoginPresenter(this);
        pd = new ProgressDialog(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading, please wait ...");
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnabled) {
                   // IMEI = "353237075301581";
                    Log.e("IMEI", IMEI);
                    pd.show();
                    loginPresenter.login(Constant.base_url + "/dtr/mobile/login", IMEI);

                } else {
                    showDialogDenied("All Permission must be enabled", 2);
                }

            }
        });
        requestAllPermision();
    }

    public void requestAllPermision() {
        if ((ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||


                (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||

                (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) ||

                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||

                ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA},
                    0);
        } else {
            isEnabled = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEI = Build.getSerial();
            } else {
                IMEI = telephonyManager.getDeviceId();
            }
            if (dbContext.getUser() != null) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        isEnabled = false;
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED


                        ) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    IMEI = telephonyManager.getDeviceId();
                    isEnabled = true;

                    if (dbContext.getUser() != null) {
                        Intent intent = new Intent(this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                    return;
                }

                requestAllPermision();
                return;
            }
        }
    }

    public void showDialogDenied(String message, final int code) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code == 1) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                } else {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }

            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                LoginActivity.this.finish();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onSuccess(UserModel userModel) {
        dbContext.insertUser(userModel);
        Intent intent = new Intent(this, Home.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        pd.dismiss();
    }

    @Override
    public JsonApi getApiInstance() {
        return JsonApi.getInstance(this);
    }

}
