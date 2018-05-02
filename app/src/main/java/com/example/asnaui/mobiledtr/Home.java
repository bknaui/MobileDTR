package com.example.asnaui.mobiledtr;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTR;
import com.example.asnaui.mobiledtr.Leave.Leave;
import com.example.asnaui.mobiledtr.Global.DBContext;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity {

    static public DBContext dbContext;
    static public String id = "";
    Toolbar toolbar;
    FragmentTransaction ft;
    FragmentManager fm;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DTR dtr = new DTR();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Daily Time Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        dbContext = new DBContext(this);
        id = dbContext.getID();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.content_frame, dtr).commit();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.dtr:
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, dtr).commit();
                                break;
                            case R.id.so:
                                break;
                            case R.id.leave:
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, new Leave()).commit();
                                break;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        navigationView.addHeaderView(setNavigationHeader());
    }

    public View setNavigationHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.navigation_header, null, false);
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
            case R.id.upload:
                dtr.presenter.addTimeLogs();
                dtr.displayList();
                return true;
            case R.id.fingerprint:
                Toast.makeText(this,"Please put your registered finger into the biometrhic scanner",Toast.LENGTH_SHORT).show();
                dtr.helper.startAuth(dtr.fingerprintManager, dtr.cryptoObject);
                /*
                dtr.presenter.addTimeLogs();
                dtr.displayList();
                */
                return true;
            default:
                return true;
        }
    }

}
