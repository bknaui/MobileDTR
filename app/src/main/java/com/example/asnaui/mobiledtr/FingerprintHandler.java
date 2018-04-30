package com.example.asnaui.mobiledtr;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private CancellationSignal cancellationSignal;
    private Context appContext;
    private FingerprintManager mManager;
    private FingerprintManager.CryptoObject mCryptoObject;


    public FingerprintHandler(Context context) {
        appContext = context;
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        mManager = manager;
        mCryptoObject = cryptoObject;
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setMessage(errString.toString());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // startAuth(mManager, mCryptoObject);
                scannerDialog("Please put your registered finger on the sensor");
            }
        });
      Dialog dialog = builder.create();
      dialog.show();
    }

    public void scannerDialog(String message) {
        Toast.makeText(appContext,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        scannerDialog(""+helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setMessage("Access denied");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //  startAuth(mManager, mCryptoObject);
                scannerDialog("Please put your registered finger on the sensor");
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        Toast.makeText(appContext,
                "Authentication succeeded.",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(appContext, Home.class);
        appContext.startActivity(intent);
    }
}
