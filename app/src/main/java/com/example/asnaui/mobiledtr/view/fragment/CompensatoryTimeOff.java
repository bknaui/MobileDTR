package com.example.asnaui.mobiledtr.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.adapter.CtoListAdapter;
import com.example.asnaui.mobiledtr.contract.CompensatoryTimeOffContract;
import com.example.asnaui.mobiledtr.contract.SwipeCallBack;
import com.example.asnaui.mobiledtr.presenter.CompensatoryTimeOffPresenter;
import com.example.asnaui.mobiledtr.util.CtoSwipCallback;
import com.example.asnaui.mobiledtr.view.activity.Home;

import java.util.ArrayList;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class CompensatoryTimeOff extends Fragment implements CompensatoryTimeOffContract.View {
    private RecyclerView listView;
    public CompensatoryTimeOffPresenter presenter;
    private CtoListAdapter adapter;
    private CtoSwipCallback ctoSwipCallback;
    public static ArrayList<String> list = new ArrayList<>();


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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        presenter = new CompensatoryTimeOffPresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setLayoutManager(linearLayoutManager);

        adapter = new CtoListAdapter(list, new SwipeCallBack.Cto() {
            @Override
            public void onItemDelete(String value) {
                Home.dbContext.deleteCTO(value);
                Toast.makeText(getContext(), "CTO Date:" + value + " deleted", Toast.LENGTH_SHORT).show();
            }
        });

        ctoSwipCallback = new CtoSwipCallback(getContext(), adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ctoSwipCallback);
        itemTouchHelper.attachToRecyclerView(listView);

        listView.setAdapter(adapter);
        display();
        return view;
    }


    @Override
    public void display() {
        Home.pd.setMessage("Loading data, please wait....");
        Home.pd.show();
        new MyLoader().execute();
    }

    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> list = Home.dbContext.getCTO();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Home.pd.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<String> dtrDates) {
            super.onPostExecute(dtrDates);
            list.clear();
            list.addAll(dtrDates);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");
            Home.pd.dismiss();
        }
    }
}
