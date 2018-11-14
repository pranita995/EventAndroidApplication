package com.example.eventapp.eventandroidapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventapp.eventandroidapplication.Adapter.EventAdpater;
import com.example.eventapp.eventandroidapplication.Model.Event;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    ArrayList<Event> eventList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public EditText editText_Agenda, editText_Date, editText_Time;
    public Boolean isUpdate = false;
    public String idUpdate = " ";
    private static final String TAG = HomepageActivity.class.getSimpleName();
    RecycleAdapter eventAdpater;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.eventRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        final String key = database.getReference("Events").push().getKey();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomepageActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);

                builder.setCancelable(false);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                editText_Agenda = (EditText) dialogView.findViewById(R.id.ed_agenda);
                editText_Date = (EditText) dialogView.findViewById(R.id.ed_date);
                editText_Time = (EditText) dialogView.findViewById(R.id.ed_time);


                final AlertDialog dialog = builder.create();

                editText_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                HomepageActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        editText_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                editText_Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(HomepageActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        editText_Time.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                });


                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        Event event = new Event();
                        event.setEvent_name(editText_Agenda.getText().toString());
                        event.setEvent_date(editText_Date.getText().toString());
                        event.setEvent_time(editText_Time.getText().toString());


                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, event.toFirebaseObject());
                        database.getReference("Events").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    finish();
                                }
                            }
                        });

                        Toast.makeText(getApplication(), "Added New Event", Toast.LENGTH_SHORT).show();

                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getApplication(), "Cancle button clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();

            }
        });


       // FirebaseDatabase database = FirebaseDatabase.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();

        database.getReference("Events").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        eventList.clear();

                        Log.w("Event Application", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("Event Application", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Event event = data.getValue(Event.class);
                            eventList.add(event);
                        }

                        eventAdpater = new RecycleAdapter();
                        recyclerView.setAdapter(eventAdpater);

                        eventAdpater.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Event Application", "getUser:onCancelled", databaseError.toException());
                    }
                });


    }


    private class RecycleAdapter extends RecyclerView.Adapter {


        @Override
        public int getItemCount() {
            return eventList.size();
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
            Event todo = eventList.get(position);
            ((SimpleItemViewHolder) holder).event_name.setText(todo.getEvent_name());
            ((SimpleItemViewHolder) holder).event_date.setText(todo.getEvent_date());
            ((SimpleItemViewHolder) holder).event_time.setText(todo.getEvent_time());
        }

        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView event_name, event_date, event_time;
            public int position;
            public SimpleItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                 event_name= (TextView) itemView.findViewById(R.id.event_name);
                 event_date= (TextView) itemView.findViewById(R.id.event_date);
                 event_time= (TextView) itemView.findViewById(R.id.event_time);

            }

            @Override
            public void onClick(View view) {

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(HomepageActivity.this,LoginActivity.class);
        startActivity(intent);

    }


}
