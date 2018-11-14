package com.example.eventapp.eventandroidapplication.Model;

import java.util.HashMap;

public class Event {

    private String Event_name, Event_date, Event_time;

    public Event() {
    }

    public Event(String event_name, String event_date, String event_time) {
        Event_name = event_name;
        Event_date = event_date;
        Event_time = event_time;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> event =  new HashMap<String,String>();
        event.put("event_name", Event_name);
        event.put("event_date", Event_date);
        event.put("event_time", Event_time);

        return event;
    }


    public String getEvent_name() {
        return Event_name;
    }

    public void setEvent_name(String event_name) {
        Event_name = event_name;
    }

    public String getEvent_date() {
        return Event_date;
    }

    public void setEvent_date(String event_date) {
        Event_date = event_date;
    }

    public String getEvent_time() {
        return Event_time;
    }

    public void setEvent_time(String event_time) {
        Event_time = event_time;
    }
}
