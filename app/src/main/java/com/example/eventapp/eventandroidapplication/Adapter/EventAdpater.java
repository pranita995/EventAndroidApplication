package com.example.eventapp.eventandroidapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventapp.eventandroidapplication.Model.Event;
import com.example.eventapp.eventandroidapplication.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdpater extends RecyclerView.Adapter {

    ArrayList<Event> eventList;

    public EventAdpater(ArrayList<Event> eventList1) {

        eventList1=this.eventList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_list_layout, parent, false);
        SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
        viewHolder.position = position;
        Event event = eventList.get(position);
        ((SimpleItemViewHolder) holder).event_name.setText(event.getEvent_name());
        ((SimpleItemViewHolder) holder).event_date.setText(event.getEvent_date());
        ((SimpleItemViewHolder) holder).event_time.setText(event.getEvent_time());
    }

    public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView event_name, event_time, event_date;
        public int position;
        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            event_time = (TextView) itemView.findViewById(R.id.event_time);

        }

        @Override
        public void onClick(View view) {

        }
    }
    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
