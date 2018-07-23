package com.example.asnaui.mobiledtr.OfficeOrder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class OfficeOrder extends Fragment implements OfficeOrderImp.View {
    ListView listView;
    public OfficeOrderPresenter presenter;
    OfficeOrderAdapter adapter;
    AdapterView.AdapterContextMenuInfo info;
    public static ArrayList<OfficeOrderItem> list = new ArrayList<>();

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
        presenter = new OfficeOrderPresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setDividerHeight(0);
        adapter = new OfficeOrderAdapter(getContext(), list);
        listView.setAdapter(adapter);
        display();
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).so);
        menu.add(Menu.NONE, 0, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Home.dbContext.deleteSO(list.get(info.position).so,list.get(info.position).date);
        display();
        Toast.makeText(getContext(),"Successfully Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public void display() {
        Home.pd.setMessage("Loading data, please wait....");
        Home.pd.show();
      new MyLoader().execute();
    }
    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<OfficeOrderItem>> {

        @Override
        protected ArrayList<OfficeOrderItem> doInBackground(Void... voids) {

            ArrayList<OfficeOrderItem> list = Home.dbContext.getSO();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Home.pd.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<OfficeOrderItem> dtrDates) {
            super.onPostExecute(dtrDates);
            list.clear();
            list.addAll(dtrDates);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");
            Home.pd.dismiss();
        }
    }
}
