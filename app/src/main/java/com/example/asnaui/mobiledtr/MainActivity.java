package com.example.asnaui.mobiledtr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.Global.DBContext;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity {




    public static Dialog dialog;
    public static Location current = null;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    public DBContext dbContext;
    public float distanceTo = 0;
    public static TextView scan;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbContext = new DBContext(this);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });

       // showLogin();
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        createLocationRequest();
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);
            }
        } else {
//            mLocationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    if (locationResult == null) {
//                        return;
//                    }
//                    for (Location location : locationResult.getLocations()) {
//                        current = location;
//                        if (current == null) {
//                            scan.setVisibility(View.GONE);
//                        } else {
//                            Log.e("CURRENT", current.getLatitude() + " " + current.getLongitude());
//                            if (isWithinRadius(current) < 1000) {
//                                scan.setVisibility(View.VISIBLE);
//                            } else {
//                                scan.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                }
//            };

//            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                    mLocationCallback,
//                    null);

        }
//        SettingsClient client = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//
//            }
//        });
//
//        task.addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (e instanceof ResolvableApiException) {
//                    try {
//                        ResolvableApiException resolvable = (ResolvableApiException) e;
//                        resolvable.startResolutionForResult(MainActivity.this,
//                                0);
//                    } catch (IntentSender.SendIntentException sendEx) {
//
//                    }
//                }
//            }
//        });

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (showLogin()) {
//                    Location center = new Location("CENTER");
//                    center.setLatitude(10.1);
//                    center.setLongitude(10.1);
//                    if (current != null) {
//                        if (isWithinRadius(current) < 10000) {
//                            scannerDialog("Please put your registered finger on the sensor");
//                        } else {
//                            scannerDialog("You are not within range of the specified location");
//                        }
//                    }
//                }
//            }
//        });
    }

//    public boolean showLogin() {
//        if (dbContext.getID().equalsIgnoreCase("")) {
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.register_id_dialog);
//            final EditText id = dialog.findViewById(R.id.id);
//            dialog.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String mId = id.getText().toString();
//                    if (mId.trim().isEmpty()) {
//                        id.setError("Required");
//                        id.requestFocus();
//                    } else {
//                        dbContext.insertUser(mId);
//                        Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                }
//            });
//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            lp.gravity = Gravity.CENTER;
//            dialog.getWindow().setAttributes(lp);
//
//            dialog.show();
//            return false;
//        }
//        return true;
//
//    }



    public void scannerDialog(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
//                    mLocationCallback = new LocationCallback() {
//                        @Override
//                        public void onLocationResult(LocationResult locationResult) {
//                            if (locationResult == null) {
//                                return;
//                            }
//                            for (Location location : locationResult.getLocations()) {
//                                current = location;
//                                if (current == null) {
//                                    scan.setVisibility(View.GONE);
//                                } else {
//                                    Log.e("CURRENT", current.getLatitude() + " " + current.getLongitude());
//                                    if (isWithinRadius(current) < 10000) {
//                                        scan.setVisibility(View.VISIBLE);
//                                    } else {
//                                        scan.setVisibility(View.GONE);
//                                    }
//                                }
//                            }
//                        }
//                    };
//
//                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                            mLocationCallback,
//                            null);
                } else {
                    showDialogDenied("Location permission must be enabled to check if you are within the specified area");
                }
                return;
            }
        }
    }

    public void showDialogDenied(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
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

    public float isWithinRadius(Location test) {
        Location center = new Location("CENTER");
        center.setLatitude(10.3076739);
        center.setLongitude(123.8931655);
        distanceTo = center.distanceTo(test);
        return distanceTo;
    }
}
