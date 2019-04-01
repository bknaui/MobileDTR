package com.example.asnaui.mobiledtr.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.asnaui.mobiledtr.adapter.LeaveListAdapter;


public class LeaveSwipCallback extends ItemTouchHelper.SimpleCallback {

    private LeaveListAdapter leaveListAdapter;
    private Drawable icon;
    private final ColorDrawable background;

    public LeaveSwipCallback(Context context, LeaveListAdapter leaveListAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.leaveListAdapter = leaveListAdapter;

        background = new ColorDrawable(Color.TRANSPARENT);
        icon = ContextCompat.getDrawable(context, android.R.drawable.ic_delete);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        leaveListAdapter.deleteItem(viewHolder.getAdapterPosition());
    }

}
