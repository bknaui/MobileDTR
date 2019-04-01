package com.example.asnaui.mobiledtr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.contract.SwipeCallBack;
import com.example.asnaui.mobiledtr.model.LeaveModel;

import java.util.List;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.AbsenceViewHolder> {

    private List<LeaveModel> list;
    private SwipeCallBack.Leave leave;

    public LeaveListAdapter(List<LeaveModel> list, SwipeCallBack.Leave leave) {
        this.list = list;
        this.leave = leave;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_template, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        holder.type.setText(list.get(position).leave_type);
        holder.inclusive_date.setText(list.get(position).inclusive_date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        leave.onItemDelete(list.get(position));
        list.remove(position);
        this.notifyItemRemoved(position);
    }

    static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView type, inclusive_date;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.leave_type);
            inclusive_date = itemView.findViewById(R.id.date);
        }
    }
}
