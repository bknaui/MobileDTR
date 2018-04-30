package com.example.asnaui.mobiledtr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.example.asnaui.mobiledtr.Helper.DtrListAdapter;
import com.example.asnaui.mobiledtr.Object.DtrDate;
import com.example.asnaui.mobiledtr.Object.DtrTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity {
    ListView listView;
    DtrListAdapter adapter;
    ArrayList<DtrDate> list = new ArrayList<>();
    public DBContext dbContext;
    String id = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_list);
        listView = findViewById(R.id.dtr_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Mobile DTR");
        listView.setDividerHeight(0);
        dbContext = new DBContext(this);
        id = dbContext.getID();

        if (!dbContext.dateExists(getCurrentDate())) {
            dbContext.insertDate(new DtrDate(id, getCurrentDate(), null));
        }
        dbContext.insertLogs(new DtrTime(getCurrentDate(), getCurrentTime(), dbContext.lastStatus(getCurrentDate())));
        list = dbContext.getDate();
        adapter = new DtrListAdapter(list, this);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upload:
                Toast.makeText(this,"Upload API is in progress",Toast.LENGTH_SHORT).show();
                return true;
                default: return true;
        }
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy");
        return mdformat.format(calendar.getTime());
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        return mdformat.format(calendar.getTime());
    }
}
