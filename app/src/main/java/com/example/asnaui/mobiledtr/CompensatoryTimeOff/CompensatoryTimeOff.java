package com.example.asnaui.mobiledtr.CompensatoryTimeOff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.Home;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOff extends Fragment implements CompensatoryTimeOffImp.View {
    ListView listView;
    public CompensatoryTimeOffPresenter presenter;
    CompensatoryTimeOffAdapter adapter;
    public static ArrayList<String> list = new ArrayList<>();
    AdapterView.AdapterContextMenuInfo info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.leave_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null, false);
        presenter = new CompensatoryTimeOffPresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setDividerHeight(0);
        display();
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position));
        menu.add(Menu.NONE, 0, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Home.dbContext.deleteCTO(list.get(info.position));
        display();
        Toast.makeText(getContext(),"Successfully Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public void display() {
        list = Home.dbContext.getCTO();
        adapter = new CompensatoryTimeOffAdapter(getContext(), list);
        listView.setAdapter(adapter);
    }
}
