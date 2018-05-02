package com.example.asnaui.mobiledtr.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTRDate;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrListAdapter extends BaseAdapter {
    ArrayList<DTRDate> dateList;
    Context context;
    LayoutInflater layoutInflater;
    String flagger = "";

    public DtrListAdapter(ArrayList<DTRDate> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int i) {
        return dateList.get(i);
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
            view = layoutInflater.inflate(R.layout.dtr_item_template, null, false);
            handler.container = view.findViewById(R.id.time_logs);
            handler.date = view.findViewById(R.id.date);
            view.setTag(handler);
        } else handler = (Handler) view.getTag();

        if (!flagger.equalsIgnoreCase(dateList.get(i).date)) {
            flagger = dateList.get(i).date;
            handler.date.setText(flagger);
        }
        if (handler.container.getChildCount() == 0) {
            for (int z = 0; z < dateList.get(i).list.size(); z++) {
                handler.container.addView(TimeLogs(dateList.get(i).list.get(z).time, dateList.get(i).list.get(z).status));
            }
        }

        return view;
    }

    public View TimeLogs(String time, String status) {
        View view = layoutInflater.inflate(R.layout.logs_item_template, null, false);
        TextView mTime = view.findViewById(R.id.time);
        TextView mStatus = view.findViewById(R.id.status);
        mTime.setText(time);
        mStatus.setText(status);
        return view;
    }

    static class Handler {
        LinearLayout container;
        TextView date;
    }

}
