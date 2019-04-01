package com.example.asnaui.mobiledtr.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.asnaui.mobiledtr.adapter.OfficeOrderListAdapter;


public class OfficeOrderSwipCallback extends ItemTouchHelper.SimpleCallback {

    private OfficeOrderListAdapter officeOrderListAdapter;
    private Drawable icon;
    private final ColorDrawable background;

    public OfficeOrderSwipCallback(Context context, OfficeOrderListAdapter officeOrderListAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.officeOrderListAdapter = officeOrderListAdapter;

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
        officeOrderListAdapter.deleteItem(viewHolder.getAdapterPosition());
    }

}
