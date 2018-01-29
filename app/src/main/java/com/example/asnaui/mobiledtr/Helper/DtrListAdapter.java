package com.example.asnaui.mobiledtr.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.Object.DtrDate;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrListAdapter extends BaseAdapter {
    ArrayList<DtrDate> list;
    Context context;
    LayoutInflater layoutInflater;

    public DtrListAdapter(ArrayList<DtrDate> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.dtr_item_layout, null, false);
        }
        return view;
    }

}
