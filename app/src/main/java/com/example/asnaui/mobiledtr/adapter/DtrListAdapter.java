package com.example.asnaui.mobiledtr.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.model.DTRDateModel;

import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrListAdapter extends BaseAdapter {
    ArrayList<DTRDateModel> dateList;
    Context context;
    LayoutInflater layoutInflater;

    public DtrListAdapter(ArrayList<DTRDateModel> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.e("MOBSTAZ", "DTRADAPTER");
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
            view = layoutInflater.inflate(R.layout.dtr_item_template, null, true);
            handler.container_in = view.findViewById(R.id.time_logs_in);
            handler.container_out = view.findViewById(R.id.time_logs_out);
            handler.date = view.findViewById(R.id.date);
            view.setTag(handler);
        } else {
            handler = (Handler) view.getTag();
        }

        handler.container_in.removeAllViews();
        handler.container_out.removeAllViews();

        handler.date.setText(dateList.get(i).formatDate());
        for (int z = 0; z < dateList.get(i).list.size(); z++) {
            if(dateList.get(i).list.get(z).status.equalsIgnoreCase("IN")){
                handler.container_in.addView(TimeLogs(dateList.get(i).list.get(z).formatToAmPm()));
            }else{
                handler.container_out.addView(TimeLogs(dateList.get(i).list.get(z).formatToAmPm()));
            }
        }
        return view;
    }

    public View TimeLogs(String time) {
        View view = layoutInflater.inflate(R.layout.logs_item_template, null, false);
        TextView mTime = view.findViewById(R.id.time);
        mTime.setText(time);
        return view;
    }

    class Handler {
        LinearLayout container_in,container_out;
        TextView date;
    }

}
