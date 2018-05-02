package com.example.asnaui.mobiledtr.Leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asnaui.mobiledtr.Helper.DtrListAdapter;
import com.example.asnaui.mobiledtr.R;

import java.util.ArrayList;

/**
 * Created by apangcatan on 02/05/2018.
 */

public class Leave extends Fragment {

    ListView listView;
    DtrListAdapter adapter;
    ArrayList<String> list = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leave_fragment,null,false);
            listView = view.findViewById(R.id.leave_list);

        return view;
    }
}
