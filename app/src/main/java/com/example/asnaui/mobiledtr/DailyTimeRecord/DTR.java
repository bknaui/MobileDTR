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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
    //
    MaterialCalendarView calendarView;

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
//        calendarView = view.findViewById(R.id.calendar_view);
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
//                Dialog timelog_dialog = new Dialog(getContext());
//                timelog_dialog.setContentView(R.layout.dialog_show_timelog);
//                TextView date = timelog_dialog.findViewById(R.id.timelog_date);
//                TextView day = timelog_dialog.findViewById(R.id.timelog_day);
//                LinearLayout timelog_container = timelog_dialog.findViewById(R.id.timelog_container);
//                String monthString = new DateFormatSymbols().getMonths()[calendarDay.getMonth()];
//                date.setText(monthString + " " + calendarDay.getYear());
//                day.setText(calendarDay.getDay() + " ");
//
//                SQLiteDatabase sqLiteDatabase = Home.dbContext.getReadableDatabase();
//                ArrayList<DTRTime> timelogs = Home.dbContext.getTimeLogs(calendarDay.getYear() + "-" + String.format("%02d", calendarDay.getMonth() + 1) + "-" + String.format("%02d", calendarDay.getDay()), sqLiteDatabase);
//                sqLiteDatabase.close();
//
//                Log.e("TIMELOG_SIZE", timelogs.size() + " ASD");
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                lp.copyFrom(timelog_dialog.getWindow().getAttributes());
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                lp.gravity = Gravity.CENTER;
//                timelog_dialog.getWindow().setAttributes(lp);
//                for (int i = 0; i < timelogs.size(); i++) {
//                    timelog_container.addView(getTimelogView(timelogs.get(i)));
//                }
//                timelog_dialog.show();
//
//            }
//        });
//        displayList();

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


    public View getTimelogView(DTRTime timelog) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.logs_item_template, null, false);
        TextView time = view.findViewById(R.id.time);
        TextView status = view.findViewById(R.id.status);

        time.setText(timelog.time);
        status.setText(timelog.status);
        return view;
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
//            List<CalendarDay> calendarDayList = new ArrayList<>();
//            for (int i = 0; i < list.size(); i++) {
//                int year = Integer.parseInt(list.get(i).date.split("-")[0]);
//                int month = Integer.parseInt(list.get(i).date.split("-")[1]);
//                int day = Integer.parseInt(list.get(i).date.split("-")[2]);
//                calendarDayList.add(CalendarDay.from(year, (month - 1), day));
//            }
//            calendarView.removeDecorators();
//            calendarView.addDecorator(new EventDecorator(getContext(), calendarDayList));


            adapter.notifyDataSetChanged();
            Log.e("Count", dtrDates.size() + " AS");

            Home.pd.dismiss();
        }
    }

}
