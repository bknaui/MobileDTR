package com.example.asnaui.mobiledtr.Leave;

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
 * Created by apangcatan on 02/05/2018.
 */

public class Leave extends Fragment implements LeaveImp.LeaveView {

    ListView listView;
    public LeavePresenter presenter;
    LeaveAdapter adapter;
    AdapterView.AdapterContextMenuInfo info;
    public static ArrayList<LeaveItem> list = new ArrayList<>();

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
        presenter = new LeavePresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setDividerHeight(0);
        displayLeave();
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).leave_type);
        menu.add(Menu.NONE, 0, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Home.dbContext.deleteLeave(list.get(info.position).leave_type,list.get(info.position).inclusive_date);
        displayLeave();
        Toast.makeText(getContext(),"Successfully Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void displayLeave() {
        list = Home.dbContext.getLeave();
        adapter = new LeaveAdapter(getContext(), list);
        listView.setAdapter(adapter);
    }
}
