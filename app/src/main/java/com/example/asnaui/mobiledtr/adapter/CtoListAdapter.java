package com.example.asnaui.mobiledtr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;
import com.example.asnaui.mobiledtr.contract.SwipeCallBack;

import java.util.List;

public class CtoListAdapter extends RecyclerView.Adapter<CtoListAdapter.AbsenceViewHolder> {

    private List<String> list;
    private SwipeCallBack.Cto cto;

    public CtoListAdapter(List<String> list, SwipeCallBack.Cto cto) {
        this.list = list;
        this.cto = cto;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cto_item_template, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        holder.inclusive_date.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        cto.onItemDelete(list.get(position));
        list.remove(position);
        this.notifyItemRemoved(position);
    }

    static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView inclusive_date;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            inclusive_date = itemView.findViewById(R.id.date);
        }
    }
}
