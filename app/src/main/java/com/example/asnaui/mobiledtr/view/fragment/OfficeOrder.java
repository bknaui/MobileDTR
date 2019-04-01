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
import com.example.asnaui.mobiledtr.adapter.OfficeOrderListAdapter;
import com.example.asnaui.mobiledtr.contract.OfficeOrderContract;
import com.example.asnaui.mobiledtr.contract.SwipeCallBack;
import com.example.asnaui.mobiledtr.model.OfficeOrderModel;
import com.example.asnaui.mobiledtr.presenter.OfficeOrderPresenter;
import com.example.asnaui.mobiledtr.util.OfficeOrderSwipCallback;
import com.example.asnaui.mobiledtr.view.activity.Home;

import java.util.ArrayList;

/**
 * Created by apangcatan on 03/05/2018.
 */

public class OfficeOrder extends Fragment implements OfficeOrderContract.View {
    private RecyclerView listView;
    private OfficeOrderListAdapter officeOrderListAdapter;
    private OfficeOrderSwipCallback officeOrderSwipCallback;

    public OfficeOrderPresenter presenter;
    public static ArrayList<OfficeOrderModel> list = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.common_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        presenter = new OfficeOrderPresenter(this, Home.dbContext);
        listView = view.findViewById(R.id.list);
        listView.setLayoutManager(linearLayoutManager);

        officeOrderListAdapter = new OfficeOrderListAdapter(list, new SwipeCallBack.OfficeOrder() {
            @Override
            public void onItemDelete(OfficeOrderModel officeOrder) {
                Home.dbContext.deleteSO(officeOrder.so, officeOrder.date);
                Toast.makeText(getContext(), "SO#:"+officeOrder.so+" deleted", Toast.LENGTH_SHORT).show();
            }
        });

        officeOrderSwipCallback = new OfficeOrderSwipCallback(getContext(), officeOrderListAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(officeOrderSwipCallback);
        itemTouchHelper.attachToRecyclerView(listView);

        listView.setAdapter(officeOrderListAdapter);

        display();
        return view;
    }

    @Override
    public void display() {
        Home.pd.setMessage("Loading data, please wait....");
        Home.pd.show();
        new MyLoader().execute();
    }

    public class MyLoader extends AsyncTask<Void, Integer, ArrayList<OfficeOrderModel>> {

        @Override
        protected ArrayList<OfficeOrderModel> doInBackground(Void... voids) {

            ArrayList<OfficeOrderModel> list = Home.dbContext.getSO();
            return list;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Home.pd.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<OfficeOrderModel> dtrDates) {
            super.onPostExecute(dtrDates);
            list.clear();
            list.addAll(dtrDates);
            officeOrderListAdapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");
            Home.pd.dismiss();
        }
    }
}
