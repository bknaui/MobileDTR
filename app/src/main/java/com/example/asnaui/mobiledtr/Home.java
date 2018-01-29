package com.example.asnaui.mobiledtr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.asnaui.mobiledtr.Helper.DtrListAdapter;
import com.example.asnaui.mobiledtr.Object.DtrDate;
import com.example.asnaui.mobiledtr.Object.DtrTime;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class Home extends AppCompatActivity {
    ListView listView;
    DtrListAdapter adapter;
    ArrayList<DtrDate> list = new ArrayList<>();
    ArrayList<DtrTime> time = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_list);
        listView = (ListView) findViewById(R.id.dtr_list);
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        list.add(new DtrDate("Jan 13", time));
        adapter = new DtrListAdapter(list, this);
        listView.setAdapter(adapter);
    }
}
