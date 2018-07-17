package com.example.asnaui.mobiledtr.CompensatoryTimeOff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class CompensatoryTimeOffAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list = new ArrayList<>();
    LayoutInflater inflater;

    public CompensatoryTimeOffAdapter(Context context, ArrayList<String> list) {
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
            view = inflater.inflate(R.layout.cto_item_template, null, false);
            handler.inclusive_date = view.findViewById(R.id.date);
            view.setTag(handler);
        } else handler = (Handler) view.getTag();
        handler.inclusive_date.setText(list.get(i));
        return view;
    }

    static class Handler {
        TextView inclusive_date;
    }
}
