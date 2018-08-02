package com.example.asnaui.mobiledtr;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final Context context;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(Context context, Collection<CalendarDay> dates) {
        this.context = context;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.date_event));
    }
}