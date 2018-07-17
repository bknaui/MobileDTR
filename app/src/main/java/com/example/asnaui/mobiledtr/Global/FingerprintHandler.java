package com.example.asnaui.mobiledtr.Global;

/**
 * Created by Asnaui on 1/23/2018.
 */
/*
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private CancellationSignal cancellationSignal;
    private Context appContext;
    DTR dtr;
    Dialog f_dialog, e_dialog;
    public TextView mMessage;

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
        dialog_scan();
        dialog_error();
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        Delay(errString.toString());
        cancellationSignal.cancel();

    }
    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        Delay("" + helpString);
        cancellationSignal.cancel();

    }

    @Override
    public void onAuthenticationFailed() {
        cancellationSignal.cancel();
        Delay("Access denied");
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        Delay("Yosha");
    }

    public void dialog_scan() {
        if (f_dialog == null) {
            f_dialog = new Dialog(appContext);
            f_dialog.setContentView(R.layout.fingerprint_dialog);
            f_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(f_dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            f_dialog.getWindow().setAttributes(lp);
        }
    }

    public void dialog_error() {
        if (e_dialog == null) {
            e_dialog = new Dialog(appContext);
            e_dialog.setContentView(R.layout.fingerprint_error_dialog);
                e_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mMessage = e_dialog.findViewById(R.id.error);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(e_dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            e_dialog.getWindow().setAttributes(lp);
        }
    }

    public void Delay(final String message) {
        f_dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                f_dialog.dismiss();
                if (message.equalsIgnoreCase("Yosha")) {
                    dtr.presenter.addTimeLogs("","","");
                    dtr.displayList();
                } else {
                    if (!message.equalsIgnoreCase("Fingerprint operation canceled.")) {
                        mMessage.setText(message);
                        e_dialog.show();
                    }
                }
            }
        }, 3000);
    }
}
*/
