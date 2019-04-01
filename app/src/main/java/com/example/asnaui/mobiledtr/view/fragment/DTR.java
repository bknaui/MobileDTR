package com.example.asnaui.mobiledtr.view.fragment;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.adapter.DtrListAdapter;
import com.example.asnaui.mobiledtr.contract.DTRContract;
import com.example.asnaui.mobiledtr.model.DTRDateModel;
import com.example.asnaui.mobiledtr.presenter.DTRPresenter;
import com.example.asnaui.mobiledtr.view.activity.Home;

import java.util.ArrayList;

/**
 * Created by apangcatan on 30/04/2018.
 */

public class DTR extends Fragment implements DTRContract.DTRView {


    private ListView listView;
    private DtrListAdapter adapter;
    public static ArrayList<DTRDateModel> list = new ArrayList<>();
    private DTRPresenter presenter;
    private Dialog dialog;
    private TextView mStatus, mTime, mLocation;


    public DTRPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new DTRPresenter(this, Home.dbContext, Home.id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dtr_list, container, false);
        mLocation = view.findViewById(R.id.location_status);
        listView = view.findViewById(R.id.dtr_list);
        listView.setDividerHeight(0);
        adapter = new DtrListAdapter(list, getContext());
        listView.invalidate();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayList();
            }
        });
        displayList();
        return view;
    }

    @Override
    public void displayList() {
        new MyLoader().execute();
    }


    @Override
    public void displayTimelogDialog(String status, String time) {
        if (dialog == null) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.fingerprint_timelog_dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mStatus = dialog.findViewById(R.id.timelog_status);
            mTime = dialog.findViewById(R.id.timelog_time);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
        }
        mStatus.setText(status);
        mTime.setText(time);
        dialog.show();
    }

    @Override
    public void displayErrorDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fingerprint_error_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void updateLocationStatus(String location, int backgroundColor) {
        if (this.mLocation == null) return;

        this.mLocation.setVisibility(View.VISIBLE);
        this.mLocation.setBackgroundColor(backgroundColor);
        this.mLocation.setText(location);
    }

    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<DTRDateModel>> {

        @Override
        protected ArrayList<DTRDateModel> doInBackground(Void... voids) {
            ArrayList<DTRDateModel> list = Home.dbContext.getDate();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(ArrayList<DTRDateModel> dtrDateModels) {
            super.onPostExecute(dtrDateModels);
            list.clear();
            list.addAll(dtrDateModels);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDateModels.size() + " AS");
        }
    }

}
