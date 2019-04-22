package com.example.asnaui.mobiledtr.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.adapter.DtrListAdapter;
import com.example.asnaui.mobiledtr.contract.DTRContract;
import com.example.asnaui.mobiledtr.model.DTRDateModel;
import com.example.asnaui.mobiledtr.presenter.DTRPresenter;
import com.example.asnaui.mobiledtr.util.LocationManagerHelper;
import com.example.asnaui.mobiledtr.util.MyBroadcastReceiver;
import com.example.asnaui.mobiledtr.view.activity.Home;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by apangcatan on 30/04/2018.
 */

public class DTR extends Fragment implements DTRContract.DTRView, ResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback callback;
    private FusedLocationProviderClient locationClient;
    LocationSettingsRequest locationSettingsRequest;
    SettingsClient settingsClient;
    LocationSettingsRequest.Builder builder;
    BroadcastReceiver myBroadcastReceiver;
    public static Location current = null;

    private ListView listView;
    private DtrListAdapter adapter;
    public static ArrayList<DTRDateModel> list = new ArrayList<>();
    private DTRPresenter presenter;
    private Dialog dialog;
    private TextView mStatus, mTime, mLocation;

    private String date, time, filePath;

    public DTRPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new DTRPresenter(this, Home.dbContext, Home.id);
        myBroadcastReceiver = new MyBroadcastReceiver(new DTRContract.LocationCallback() {
            @Override
            public void setLocation(Location location) {
                setCurrentLocation(location);
            }

            @Override
            public void updateStatus(String message, int color) {
                updateLocationStatus(message, color);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        getContext().registerReceiver(myBroadcastReceiver, intentFilter);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dtr_list, container, false);
        mLocation = view.findViewById(R.id.location_status);
        listView = view.findViewById(R.id.dtr_list);
        listView.setDividerHeight(0);
        adapter = new DtrListAdapter(list, getContext());
        listView.invalidate();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayList();
            }
        });
        displayList();
        init_broadcast();
        return view;
    }


    protected void init_broadcast() {

        if (!LocationManagerHelper.isLocationEnabled(getContext())) {
            updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(getContext(), R.color.gps_disabled));
            Log.e("Location","Disabled");
        } else {
            updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(getContext(), R.color.callibrating));
            Log.e("Location","Enabled");
        }

        startLocationUpdates();
    }


    @Override
    public void displayList() {
        new MyLoader().execute();
    }

    public void showDialogDenied(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void displayTimelogDialog(String status, String time) {
        if (dialog == null) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.fingerprint_timelog_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mStatus = dialog.findViewById(R.id.timelog_status);
            mTime = dialog.findViewById(R.id.timelog_time);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
        }
        mStatus.setText(status);
        mTime.setText(time);
        dialog.show();
    }

    @Override
    public void displayErrorDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fingerprint_error_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    public void initiateImageCapture() {
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), ".MobileDTR/MyImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, Home.userModel.id + "_" + timeStamp + ".png");
        filePath = image.getAbsolutePath();

        Uri uriSavedImage = FileProvider.getUriForFile(getContext(), getContext().getPackageName() +
                ".my.package.name.provider", new File(imagesFolder, Home.userModel.id + "_" + timeStamp + ".png"));
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        date = timeStamp.split("_")[0] + "-" + timeStamp.split("_")[1] + "-" + timeStamp.split("_")[2];
        time = timeStamp.split("_")[3] + ":" + timeStamp.split("_")[4] + ":" + timeStamp.split("_")[5];
        if(imageIntent.resolveActivity(getContext().getPackageManager()) != null){
            startActivityForResult(imageIntent, 1);
        }else{
            Toast.makeText(getContext(), "No available camera to use", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(getContext(), R.color.callibrating));
                } else {

                    updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(getContext(), R.color.gps_disabled));
                }
                break;
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    if (current != null) {
                        String latitude = String.valueOf(current.getLatitude());
                        String longitude = String.valueOf(current.getLongitude());
                        getPresenter().addTimeLogs(date, time, filePath, latitude, longitude);
                    } else {
                        displayErrorDialog();
                    }
                }
                break;
            case 0:
                if (resultCode == RESULT_CANCELED) {
                    Log.e("CANCELLED", "Cancel");
                    setCurrentLocation(null);
                    updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(getContext(), R.color.gps_disabled));
                } else {
                    updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(getContext(), R.color.callibrating));
                }
                break;
        }
    }

    @Override
    public void updateLocationStatus(String location, int backgroundColor) {
        this.mLocation.setVisibility(View.VISIBLE);
        this.mLocation.setBackgroundColor(backgroundColor);
        this.mLocation.setText(location);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult((Activity) getContext(), 100);
                } catch (IntentSender.SendIntentException e) {
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    protected void startLocationUpdates() {

        locationClient = getFusedLocationProviderClient(getContext());
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);

        builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        locationSettingsRequest = builder.build();

        settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("FusedApi","Disabled");
            return;
        }
        Log.e("FusedApi","Enabled");
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        locationClient.requestLocationUpdates(mLocationRequest, callback, Looper.myLooper());
    }


    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        this.updateLocationStatus("Location/GPS acquired", ContextCompat.getColor(getContext(), R.color.location_acquired));
    }

    public static void setCurrentLocation(Location current) {
        DTR.current = current;
    }

    public void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    onLocationChanged(location);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MapDemoActivity", "Error trying to get last Location/GPS");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showDialogDenied("Location Permission must be enabled");
                }
                return;
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("CLOSED", "CLOSED");
        getContext().unregisterReceiver(myBroadcastReceiver);
        locationClient.removeLocationUpdates(callback);
        super.onDestroy();
    }


    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<DTRDateModel>> {

        @Override
        protected ArrayList<DTRDateModel> doInBackground(Void... voids) {
            ArrayList<DTRDateModel> list = Home.dbContext.getDate();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(ArrayList<DTRDateModel> dtrDateModels) {
            super.onPostExecute(dtrDateModels);
            list.clear();
            list.addAll(dtrDateModels);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDateModels.size() + " AS");
        }
    }

}
