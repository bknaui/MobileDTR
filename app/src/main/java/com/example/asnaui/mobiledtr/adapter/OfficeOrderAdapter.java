package com.example.asnaui.mobiledtr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.model.OfficeOrderModel;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class OfficeOrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<OfficeOrderModel> list = new ArrayList<>();
    LayoutInflater inflater;

    public OfficeOrderAdapter(Context context, ArrayList<OfficeOrderModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        Handler handler;
        if (view == null) {
            handler = new Handler();
            view = inflater.inflate(R.layout.so_item_template, null, false);
            handler.inclusive_date = view.findViewById(R.id.date);
            handler.type = view.findViewById(R.id.sick_type);
            view.setTag(handler);
        } else handler = (Handler) view.getTag();
        handler.type.setText(list.get(i).so);
        handler.inclusive_date.setText(list.get(i).date);
        return view;
    }

    static class Handler {
        TextView type, inclusive_date;
    }
}
