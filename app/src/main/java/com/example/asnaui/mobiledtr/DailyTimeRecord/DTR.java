package com.example.asnaui.mobiledtr.DailyTimeRecord;

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

import com.example.asnaui.mobiledtr.Home;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by apangcatan on 30/04/2018.
 */

public class DTR extends Fragment implements DTRImp.DTRView {


    ListView listView;
    DtrListAdapter adapter;
    public static ArrayList<DTRDate> list = new ArrayList<>();
    public DTRPresenter presenter;
    Dialog dialog;
    TextView mStatus, mTime;

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
        View view = inflater.inflate(R.layout.dtr_list, null, false);
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
        Home.pd.setMessage("Loading data, please wait....");
        Home.pd.show();
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


    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<DTRDate>> {

        @Override
        protected ArrayList<DTRDate> doInBackground(Void... voids) {
            ArrayList<DTRDate> list = Home.dbContext.getDate();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Home.pd.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<DTRDate> dtrDates) {
            super.onPostExecute(dtrDates);
            list.clear();
            list.addAll(dtrDates);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");
            Home.pd.dismiss();
        }
    }

}
