package com.example.asnaui.mobiledtr.Global;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTR;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private CancellationSignal cancellationSignal;
    private Context appContext;
    private FingerprintManager mManager;
    private FingerprintManager.CryptoObject mCryptoObject;
    DTR dtr;

    public FingerprintHandler(Context context, DTR dtr) {
        appContext = context;
        this.dtr = dtr;
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
        scannerDialog(errString.toString());
//        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
//        builder.setMessage(errString.toString());
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                scannerDialog("Please put your registered finger on the sensor");
//            }
//        });
//        Dialog dialog = builder.create();
//        dialog.show();
    }

    public void scannerDialog(String message) {
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        scannerDialog("" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        scannerDialog("Access denied");

//        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
//        builder.setMessage("Access denied");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                scannerDialog("Access denied");
//            }
//        });
//        Dialog dialog = builder.create();
//        dialog.show();
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        dtr.presenter.addTimeLogs();
        dtr.displayList();

    }
}
