package com.example.asnaui.mobiledtr;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.CompensatoryTimeOff.CompensatoryTimeOff;
import com.example.asnaui.mobiledtr.DailyTimeRecord.DTR;
import com.example.asnaui.mobiledtr.Global.Constant;
import com.example.asnaui.mobiledtr.Global.DBContext;
import com.example.asnaui.mobiledtr.Global.JsonAPi;
import com.example.asnaui.mobiledtr.Global.User;
import com.example.asnaui.mobiledtr.Leave.Leave;
import com.example.asnaui.mobiledtr.Leave.LeaveItem;
import com.example.asnaui.mobiledtr.OfficeOrder.OfficeOrder;
import com.example.asnaui.mobiledtr.OfficeOrder.OfficeOrderItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity {


    String date, time, filePath;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    public static Location current = null;

    public static ProgressDialog pd;
    static public DBContext dbContext;
    static public String id = "";
    Toolbar toolbar;
    FragmentTransaction ft;
    FragmentManager fm;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static DTR dtr = new DTR();
    public static Leave leave = new Leave();
    public static OfficeOrder oo = new OfficeOrder();
    public static CompensatoryTimeOff cto = new CompensatoryTimeOff();
    Dialog dialog;
    public static User user;
    public static TextView mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        pd = new ProgressDialog(this);
        mLocation = findViewById(R.id.location);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Daily Time Record");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        dbContext = new DBContext(this);
        user = dbContext.getUser();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.content_frame, dtr).commit();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dtr:
                                toolbar.setTitle("Daily Time Record");
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, dtr).commit();
                                break;
                            case R.id.cto:
                                toolbar.setTitle("Compensatory Time Off");
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, cto).commit();
                                break;
                            case R.id.so:
                                toolbar.setTitle("Office Order");
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, oo).commit();
                                break;
                            case R.id.leave:
                                toolbar.setTitle("Leave");
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, leave).commit();
                                break;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        navigationView.addHeaderView(setNavigationHeader());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Home.current = location;
                    if (Home.current == null) {
                        // mLocation.setText("Location Callibrating... ... ");
                        /// mLocation.setVisibility(View.VISIBLE);
                    } else {
                        if (Constant.isWithinRadius(current) < 150) {
                            //  mLocation.setVisibility(View.GONE);
                        } else {
                            //   mLocation.setText("Not Within Range");
                            //   mLocation.setVisibility(View.VISIBLE);
                        }
                        //  mLocation.setText(Home.current.getLatitude() + " " + Home.current.getLongitude() + " - " + Constant.isWithinRadius(current));

                        Log.e("CURRENT", Constant.isWithinRadius(current) + " OKAY");
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showDialogDenied("Location Permission must be enabled");

        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null);


    }

    public String GET_CURRENT_MAC() {

        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            return wInfo.getBSSID();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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

    public View setNavigationHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null, false);
        TextView name = view.findViewById(R.id.name);
        name.setText(user.name);
        return view;
    }

    public void showDialogDenied(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                Home.this.finish();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.leave:
                Fragment fragment = fm.findFragmentById(R.id.content_frame);
                if (fragment instanceof Leave) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.leave_add_dialog);

                    final Spinner sick_type = dialog.findViewById(R.id.leave_type);
                    final TextView from = dialog.findViewById(R.id.leave_from);
                    final TextView to = dialog.findViewById(R.id.leave_to);
                    final int[] from_year = new int[1];
                    final int[] from_month = new int[1];
                    final int[] from_day = new int[1];
                    final Calendar fromCalendar = Calendar.getInstance();

                    from.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            from_year[0] = year;
                                            from_month[0] = monthOfYear;
                                            from_day[0] = dayOfMonth;
                                            from.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                            fromCalendar.set(from_year[0], from_month[0], from_day[0]);
                                        }
                                    },
                                    fromCalendar.get(Calendar.YEAR),
                                    fromCalendar.get(Calendar.MONTH),
                                    fromCalendar.get(Calendar.DAY_OF_MONTH)
                            );
                            dpd.show(getFragmentManager(), "Datepickerdialog");
                        }
                    });


                    to.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();

                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            to.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                        }
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                            );

                            dpd.setMinDate(fromCalendar);
                            dpd.show(getFragmentManager(), "Datepickerdialog");

                        }
                    });

                    dialog.findViewById(R.id.save_leave).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String date = from.getText().toString() + "-" + to.getText().toString();
                            String sick = sick_type.getSelectedItem().toString();
                            if (from.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "From date must be specified", Toast.LENGTH_SHORT).show();
                            } else if (to.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "To date must be specified", Toast.LENGTH_SHORT).show();
                            } else {
                                leave.presenter.addLeave(new LeaveItem(sick, date));
                                dialog.dismiss();
                            }
                        }
                    });
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);
                    dialog.show();
                } else if (fragment instanceof OfficeOrder) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.so_add_dialog);

                    final EditText sick_type = dialog.findViewById(R.id.leave_type);
                    final TextView from = dialog.findViewById(R.id.leave_from);
                    final TextView to = dialog.findViewById(R.id.leave_to);

                    final int[] from_year = new int[1];
                    final int[] from_month = new int[1];
                    final int[] from_day = new int[1];
                    final Calendar fromCalendar = Calendar.getInstance();

                    from.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            from_year[0] = year;
                                            from_month[0] = monthOfYear;
                                            from_day[0] = dayOfMonth;
                                            from.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                            fromCalendar.set(from_year[0], from_month[0], from_day[0]);
                                        }
                                    },
                                    fromCalendar.get(Calendar.YEAR),
                                    fromCalendar.get(Calendar.MONTH),
                                    fromCalendar.get(Calendar.DAY_OF_MONTH)
                            );
                            dpd.show(getFragmentManager(), "Datepickerdialog");
                        }
                    });


                    to.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();

                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            to.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                        }
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                            );

                            dpd.setMinDate(fromCalendar);
                            dpd.show(getFragmentManager(), "Datepickerdialog");

                        }
                    });

                    dialog.findViewById(R.id.save_leave).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String date = from.getText().toString() + "-" + to.getText().toString();
                            String sick = sick_type.getText().toString();
                            if (sick.trim().isEmpty()) {
                                sick_type.setError("Required");
                                sick_type.requestFocus();
                            } else if (from.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "From date must be specified", Toast.LENGTH_SHORT).show();
                            } else if (to.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "To date must be specified", Toast.LENGTH_SHORT).show();
                            } else {
                                oo.presenter.add(new OfficeOrderItem(sick, date));
                                dialog.dismiss();
                            }
                        }
                    });
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);
                    dialog.show();
                } else if (fragment instanceof CompensatoryTimeOff) {
                    final int[] from_year = new int[1];
                    final int[] from_month = new int[1];
                    final int[] from_day = new int[1];
                    final Calendar fromCalendar = Calendar.getInstance();

                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.cto_add_dialog);

                    final TextView from = dialog.findViewById(R.id.leave_from);
                    final TextView to = dialog.findViewById(R.id.leave_to);


                    from.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            from_year[0] = year;
                                            from_month[0] = monthOfYear;
                                            from_day[0] = dayOfMonth;
                                            from.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                            fromCalendar.set(from_year[0], from_month[0], from_day[0]);
                                        }
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                            );
                            dpd.show(getFragmentManager(), "Datepickerdialog");
                        }
                    });


                    to.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar calendar = Calendar.getInstance();

                            DatePickerDialog dpd = DatePickerDialog.newInstance(
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                            to.setText(String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth) + "/" + year);
                                        }
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                            );

                            dpd.setMinDate(fromCalendar);
                            dpd.show(getFragmentManager(), "Datepickerdialog");

                        }
                    });
                    dialog.findViewById(R.id.save_leave).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String date = from.getText().toString() + "-" + to.getText().toString();
                            if (from.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "From date must be specified", Toast.LENGTH_SHORT).show();
                            } else if (to.getText().toString().isEmpty()) {
                                Toast.makeText(view.getContext(), "To date must be specified", Toast.LENGTH_SHORT).show();
                            } else {
                                cto.presenter.add(date);
                                dialog.dismiss();
                            }
                        }
                    });

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);
                    dialog.show();
                }

                return true;
            case R.id.upload:
                fragment = fm.findFragmentById(R.id.content_frame);
                if (fragment instanceof DTR) {
                    if (dtr.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonAPi.getInstance(this).InsertLogs(Constant.base_url + "/dtr/mobile/add-logs", 0, 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof Leave) {
                    if (leave.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonAPi.getInstance(this).InsertLeave(Constant.base_url + "/dtr/mobile/add-leave", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof OfficeOrder) {
                    if (oo.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonAPi.getInstance(this).InsertSO(Constant.base_url + "/dtr/mobile/add-so", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof CompensatoryTimeOff) {
                    if (cto.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonAPi.getInstance(this).InsertCTO(Constant.base_url + "/dtr/mobile/add-cto", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case R.id.fingerprint:
                if (GET_CURRENT_MAC() != null) {
                        if (GET_CURRENT_MAC().equalsIgnoreCase("ec:e1:a9:87:5a:70")) {
                        if (Constant.isTimeAutomatic(this)) {
                            if (current != null) {
                                if (Constant.isWithinRadius(current) < 100) {
                                    Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    String timeStamp = new SimpleDateFormat("yyyy_MM_dd#HH_mm_ss").format(new Date());
                                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), ".MobileDTR/MyImages");
                                    imagesFolder.mkdirs();
                                    File image = new File(imagesFolder, user.id + "_" + timeStamp + ".png");
                                    filePath = image.getAbsolutePath();
                                    Uri uriSavedImage = Uri.fromFile(image);
                                    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                                    date = timeStamp.split("#")[0].split("_")[0] + "-" + timeStamp.split("#")[0].split("_")[1] + "-" + timeStamp.split("#")[0].split("_")[2];
                                    time = timeStamp.split("#")[1].split("_")[0] + ":" + timeStamp.split("#")[1].split("_")[1] + ":" + timeStamp.split("#")[1].split("_")[2];

                                    startActivityForResult(imageIntent, 1);
                                } else {
                                    Toast.makeText(this, "You are not within range", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Cannot acquire Location or please enable Location GPS", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Automatic Date and Time must be enabled", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please connect to CHD7_WiFi", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Not connected to a WiFi", Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentFromCamera) {
        super.onActivityResult(requestCode, resultCode, intentFromCamera);
        Log.e("Check", requestCode + " " + resultCode);
        switch (requestCode) {
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    dtr.presenter.addTimeLogs(date, time, filePath);
                }
                break;
            case 0:
                mLocation.setVisibility(View.VISIBLE);
                if (resultCode == RESULT_CANCELED) {
                    Log.e("CANCELLED", "Cancel");
                    current = null;
                    mLocation.setText("GPS not enbaled");

                } else {
                    mLocation.setText("Location Callibrating... ");
                }
                break;
        }
    }
}
