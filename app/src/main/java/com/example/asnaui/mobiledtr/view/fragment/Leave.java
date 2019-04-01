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
import com.example.asnaui.mobiledtr.adapter.LeaveListAdapter;
import com.example.asnaui.mobiledtr.contract.LeaveContract;
import com.example.asnaui.mobiledtr.contract.SwipeCallBack;
import com.example.asnaui.mobiledtr.model.LeaveModel;
import com.example.asnaui.mobiledtr.presenter.LeavePresenter;
import com.example.asnaui.mobiledtr.util.LeaveSwipCallback;
import com.example.asnaui.mobiledtr.view.activity.Home;

import java.util.ArrayList;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class Leave extends Fragment implements LeaveContract.LeaveView {

    private RecyclerView listView;
    public LeavePresenter presenter;
    private LeaveListAdapter adapter;
    private LeaveSwipCallback leaveSwipCallback;
    public static ArrayList<LeaveModel> list = new ArrayList<>();

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

        presenter = new LeavePresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setLayoutManager(linearLayoutManager);

        adapter = new LeaveListAdapter(list, new SwipeCallBack.Leave() {
            @Override
            public void onItemDelete(LeaveModel leave) {
                Home.dbContext.deleteLeave(leave.leave_type, leave.inclusive_date);
                Toast.makeText(getContext(), "Leave:"+leave.leave_type+" deleted", Toast.LENGTH_SHORT).show();
            }
        });

        leaveSwipCallback = new LeaveSwipCallback(getContext(), adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(leaveSwipCallback);
        itemTouchHelper.attachToRecyclerView(listView);

        listView.setAdapter(adapter);

        displayLeave();
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void displayLeave() {
        Home.pd.setMessage("Loading data, please wait....");
        Home.pd.show();
        new MyLoader().execute();

    }

    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<LeaveModel>> {

        @Override
        protected ArrayList<LeaveModel> doInBackground(Void... voids) {

            ArrayList<LeaveModel> list = Home.dbContext.getLeave();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Home.pd.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<LeaveModel> dtrDates) {
            super.onPostExecute(dtrDates);
            list.clear();
            list.addAll(dtrDates);
            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");
            Home.pd.dismiss();
        }
    }
}
