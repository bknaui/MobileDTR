package com.example.asnaui.mobiledtr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asnaui.mobiledtr.Global.Constant;
import com.example.asnaui.mobiledtr.Global.DBContext;
import com.example.asnaui.mobiledtr.Global.JsonAPi;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    public static Dialog dialog;
    public DBContext dbContext;
    boolean isEnabled = false;
    public static ProgressDialog pd;
    int count = 0;
    String IMEI = "";
    TextView imei;
    TelephonyManager telephonyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*
        setContentView(R.layout.calendar_view);
        MaterialCalendarView calendarView = findViewById(R.id.calendar_view);
        List<CalendarDay> list = new ArrayList<>();
        list.add(CalendarDay.from(2018, 7, 13));
        list.add(CalendarDay.from(2018, 7, 14));
        list.add(CalendarDay.from(2018, 7, 15));
        calendarView.addDecorator(new EventDecorator(this, list));
        */


        setContentView(R.layout.activity_main);

        dbContext = new DBContext(this);
        pd = new ProgressDialog(this);
        imei = findViewById(R.id.login_pin);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        pd = new ProgressDialog(this);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnabled) {
                    JsonAPi.getInstance(MainActivity.this).Login(Constant.base_url + "/dtr/mobile/login", IMEI);
                } else {
                    showDialogDenied("All Permission must be enabled", 2);
                }

            }
        });
        requestAllPermision();
    }

    public void requestAllPermision() {
        if ((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||

                (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||

                (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        } else {
            isEnabled = true;
            IMEI = telephonyManager.getDeviceId();
            imei.setText(IMEI);
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
                        grantResults[2] == PackageManager.PERMISSION_GRANTED
                        ) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    IMEI = telephonyManager.getDeviceId();
                    isEnabled = true;
                    imei.setText(IMEI);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code == 0) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                } else if (code == 1) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }

            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public int VERSION_CODE() {
        int version_code = -1;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version_code = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version_code;
    }

    public void compareVersion() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.100.14/pis/CheckVersion", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equalsIgnoreCase(VERSION_CODE() + "")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Notice!");
                    builder.setMessage("WFP Tracking App v" + response + " is now available, please update your app." +
                            "\n\nNote: Updating will close the application to apply changes.");
                    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pd = ProgressDialog.show(MainActivity.this, "Downloading", "Please wait...", false, false);
                            downloadAndInstallApk();
                        }
                    });
                    builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No network connection : Offline Mode", Toast.LENGTH_SHORT).show();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                320000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(stringRequest);
    }

    public void downloadAndInstallApk() {
        try {
            final String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mobile_dtr.apk";
            final Uri uri = Uri.parse("file://" + destination);

            File file = new File(destination);
            if (file.exists()) file.delete();

            String url = "http://192.168.100.14/pis/public/apk/app-debug.apk";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Download new version of the App");
            request.setTitle("WFP Tracking");

            request.setDestinationUri(uri);

            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                        pd.dismiss();

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(new File(destination)),
                                "application/vnd.android.package-archive");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

            registerReceiver(onComplete, filter);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            pd.dismiss();
        }
    }
}
