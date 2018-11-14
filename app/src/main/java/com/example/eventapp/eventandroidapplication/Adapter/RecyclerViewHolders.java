package com.example.eventapp.eventandroidapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eventapp.eventandroidapplication.Model.Event;
import com.example.eventapp.eventandroidapplication.R;

import java.util.List;

class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    TextView event_name, event_time, event_date;
    private List<Event> taskObject;
    public RecyclerViewHolders(final View itemView, final List<Event> taskObject) {
        super(itemView);
        this.taskObject = taskObject;
        event_name = (TextView) itemView.findViewById(R.id.event_name);
        event_date = (TextView) itemView.findViewById(R.id.event_date);
        event_time = (TextView) itemView.findViewById(R.id.event_time);

    }
}
