package com.example.asnaui.mobiledtr.view.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.model.LeaveModel;
import com.example.asnaui.mobiledtr.model.OfficeOrderModel;
import com.example.asnaui.mobiledtr.model.UserModel;
import com.example.asnaui.mobiledtr.network.JsonApi;
import com.example.asnaui.mobiledtr.util.Constant;
import com.example.asnaui.mobiledtr.util.LocationManagerHelper;
import com.example.asnaui.mobiledtr.util.MyBroadcastReceiver;
import com.example.asnaui.mobiledtr.view.fragment.CompensatoryTimeOff;
import com.example.asnaui.mobiledtr.view.fragment.DTR;
import com.example.asnaui.mobiledtr.view.fragment.Leave;
import com.example.asnaui.mobiledtr.view.fragment.OfficeOrder;
import com.example.asnaui.mobiledtr.view.fragment.Tutorial;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback {


    private String date, time, filePath;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback callback;
    private FusedLocationProviderClient locationClient;

    public static Location current = null;
    public static ProgressDialog pd;
    static public DBContext dbContext;

    static public String id = "";
    private static Toolbar toolbar;
    FragmentTransaction ft;
    FragmentManager fm;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static DTR dtr = new DTR();
    public static Leave leave = new Leave();
    public static OfficeOrder oo = new OfficeOrder();
    public static Tutorial tutorial = new Tutorial();
    public static CompensatoryTimeOff cto = new CompensatoryTimeOff();
    Dialog dialog;
    public static UserModel userModel;
    ListView menu;
    BroadcastReceiver myBroadcastReceiver;

    LocationSettingsRequest locationSettingsRequest;
    SettingsClient settingsClient;
    LocationSettingsRequest.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        toolbar.setTitle("Daily Time Record");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        dbContext = new DBContext(this);
        pd = new ProgressDialog(this);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
        ft.add(R.id.content_frame, dtr).commit();
        userModel = dbContext.getUser();


        //TABLET SETTINGS
        if (navigationView != null) {
            drawerLayout = findViewById(R.id.drawer_layout);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
                            switch (menuItem.getItemId()) {
                                case R.id.dtr:
                                    toolbar.setTitle("Daily Time Record");
                                    ft.replace(R.id.content_frame, dtr).commit();
                                    break;
                                case R.id.cto:
                                    toolbar.setTitle("Compensatory Time Off");
                                    ft.replace(R.id.content_frame, cto).commit();
                                    break;
                                case R.id.so:
                                    toolbar.setTitle("Office Order");
                                    ft.replace(R.id.content_frame, oo).commit();
                                    break;
                                case R.id.leave:
                                    toolbar.setTitle("Leave");
                                    ft.replace(R.id.content_frame, leave).commit();
                                    break;
                            }
                            menuItem.setChecked(true);
                            drawerLayout.closeDrawers();
                            return true;
                        }
                    });
            navigationView.addHeaderView(setNavigationHeader());
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            menu = findViewById(R.id.menu_list);
            ArrayList<String> menu_item = new ArrayList<>();
            menu_item.add("Daily Time Record");
            menu_item.add("Leave");
            menu_item.add("Office Order");
            menu_item.add("Compensatory Time Off");
            ArrayAdapter<String> menuAdater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu_item);
            menu.setAdapter(menuAdater);
            menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            toolbar.setTitle("Daily Time Record");
                            ft = fm.beginTransaction();
                            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                            ft.replace(R.id.content_frame, dtr).commit();
                            break;
                        case 1:
                            toolbar.setTitle("Leave");
                            ft = fm.beginTransaction();
                            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                            ft.replace(R.id.content_frame, leave).commit();
                            break;
                        case 2:
                            toolbar.setTitle("Office Order");
                            ft = fm.beginTransaction();
                            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                            ft.replace(R.id.content_frame, oo).commit();
                            break;
                        case 3:
                            toolbar.setTitle("Compensatory Time Off");
                            ft = fm.beginTransaction();
                            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                            ft.replace(R.id.content_frame, cto).commit();
                            break;
                    }
                }
            });
            setNavigationHeader();
        }

        init_broadcast();

    }

    public static void updateLocationStatus(String location, int backgroundColor) {
        dtr.updateLocationStatus(location, backgroundColor);
    }


    protected void init_broadcast() {

        if (!LocationManagerHelper.isLocationEnabled(this)) {
            updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(this, R.color.gps_disabled));
        } else {
            updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(this, R.color.callibrating));
        }

        myBroadcastReceiver = new MyBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(myBroadcastReceiver, intentFilter);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, callback, Looper.myLooper());

    }

    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        updateLocationStatus("Location/GPS acquired", ContextCompat.getColor(this, R.color.location_acquired));
    }

    public void getLastLocation() {
        locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public View setNavigationHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null, false);
        TextView name = view.findViewById(R.id.name);
        name.setText(userModel.name);
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
                                leave.presenter.addLeave(new LeaveModel(sick, date));
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
                                oo.presenter.add(new OfficeOrderModel(sick, date));
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
                        JsonApi.getInstance(this).InsertLogs(Constant.base_url + "/dtr/mobile/add-logs", 0, 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof Leave) {
                    if (leave.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonApi.getInstance(this).InsertLeave(Constant.base_url + "/dtr/mobile/add-leave", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof OfficeOrder) {
                    if (oo.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonApi.getInstance(this).InsertSO(Constant.base_url + "/dtr/mobile/add-so", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }

                } else if (fragment instanceof CompensatoryTimeOff) {
                    if (cto.list.size() > 0) {
                        pd.setMessage("Uploading, please wait ...");
                        pd.show();
                        JsonApi.getInstance(this).InsertCTO(Constant.base_url + "/dtr/mobile/add-cto", 0);
                    } else {
                        Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case R.id.fingerprint:
                if (Constant.isTimeAutomatic(this)) {
                    if (current != null) {
                        initiateImageCapture();
                    } else {
                        getLastLocation();
                        if (current != null) {
                            initiateImageCapture();
                        } else {
                            Toast.makeText(this, "Cannot acquire Location or please enable Location/GPS", Toast.LENGTH_SHORT).show();
                            settingsClient = LocationServices.getSettingsClient(this);
                        }
                    }
                } else {
                    Toast.makeText(this, "Automatic Date and Time must be enabled", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return true;
        }
    }

    public void initiateImageCapture() {
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), ".MobileDTR/MyImages");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, userModel.id + "_" + timeStamp + ".png");
        filePath = image.getAbsolutePath();

        Uri uriSavedImage = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.package.name.provider", new File(imagesFolder, userModel.id + "_" + timeStamp + ".png"));
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        date = timeStamp.split("_")[0] + "-" + timeStamp.split("_")[1] + "-" + timeStamp.split("_")[2];
        time = timeStamp.split("_")[3] + ":" + timeStamp.split("_")[4] + ":" + timeStamp.split("_")[5];

        startActivityForResult(imageIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentFromCamera) {
        super.onActivityResult(requestCode, resultCode, intentFromCamera);
        Log.e("Check", requestCode + " " + resultCode);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(this, R.color.callibrating));
                } else {

                    updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(this, R.color.gps_disabled));
                }
                break;
            case 1:
                if (resultCode != RESULT_CANCELED) {
                    if (current != null) {
                        String latitude = String.valueOf(current.getLatitude());
                        String longitude = String.valueOf(current.getLongitude());
                        dtr.getPresenter().addTimeLogs(date, time, filePath, latitude, longitude);
                    } else {
                        dtr.displayErrorDialog();
                    }
                }
                break;
            case 0:
                if (resultCode == RESULT_CANCELED) {
                    Log.e("CANCELLED", "Cancel");
                    setCurrentLocation(null);
                    updateLocationStatus("Location/GPS not enabled", ContextCompat.getColor(this, R.color.gps_disabled));

                } else {
                    updateLocationStatus("Location/GPS Callibrating..", ContextCompat.getColor(this, R.color.callibrating));
                }
                break;
        }
    }


    public static void setCurrentLocation(Location current) {
        Home.current = current;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("CLOSED", "CLOSED");
        unregisterReceiver(myBroadcastReceiver);
        locationClient.removeLocationUpdates(callback);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(this, 100);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
}
