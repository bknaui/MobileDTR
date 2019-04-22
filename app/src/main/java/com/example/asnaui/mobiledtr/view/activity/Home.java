package com.example.asnaui.mobiledtr.view.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.example.asnaui.mobiledtr.view.fragment.CompensatoryTimeOff;
import com.example.asnaui.mobiledtr.view.fragment.DTR;
import com.example.asnaui.mobiledtr.view.fragment.Leave;
import com.example.asnaui.mobiledtr.view.fragment.OfficeOrder;
import com.example.asnaui.mobiledtr.view.fragment.Tutorial;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity {


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
        drawerLayout = findViewById(R.id.drawer_layout);

        //TABLET SETTINGS
        if (drawerLayout != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.dtr:
                                toolbar.setTitle("Daily Time Record");
                                ft = fm.beginTransaction();
                                ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
                                ft.replace(R.id.content_frame, dtr).commit();
                                break;
                            case R.id.cto:
                                toolbar.setTitle("Compensatory Time Off");
                                ft = fm.beginTransaction();
                                ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
                                ft.replace(R.id.content_frame, cto).commit();
                                break;
                            case R.id.so:
                                toolbar.setTitle("Office Order");
                                ft = fm.beginTransaction();
                                ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
                                ft.replace(R.id.content_frame, oo).commit();
                                break;
                            case R.id.leave:
                                toolbar.setTitle("Leave");
                                ft = fm.beginTransaction();
                                ft.setCustomAnimations(R.animator.enter_from_left, R.animator.exit_to_right, R.animator.enter_from_right, R.animator.exit_to_left);
                                ft.replace(R.id.content_frame, leave).commit();
                                break;
                        }
                        menuItem.setChecked(true);
                        if(drawerLayout != null){
                            drawerLayout.closeDrawers();
                        }

                        return true;
                    }
                });
        navigationView.addHeaderView(setNavigationHeader());


        // init_broadcast();

    }

    public View setNavigationHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null, false);
        TextView name = view.findViewById(R.id.name);
        name.setText(userModel.name);
        return view;
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
            case R.id.fingerprint:
                if (Constant.isTimeAutomatic(this)) {
                    if (dtr.current != null) {
                        dtr.initiateImageCapture();
                    } else {
                        dtr.getLastLocation();
                        if (dtr.current != null) {
                            dtr.initiateImageCapture();
                        } else {
                            Toast.makeText(this, "Cannot acquire Location or please enable Location/GPS", Toast.LENGTH_SHORT).show();
                            //  dtr.settingsClient = LocationServices.getSettingsClient(this);
                        }
                    }
                } else {
                    Toast.makeText(this, "Automatic Date and Time must be enabled", Toast.LENGTH_SHORT).show();
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

            default:
                return true;
        }
    }
}
