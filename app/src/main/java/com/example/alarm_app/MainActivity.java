package com.example.alarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    TimePickerDialog timePicker;
    public int tHour, tMinute;
    private MyBroadcastReceiver alarm;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        timeView = findViewById(R.id.timeView);
        alarm = new MyBroadcastReceiver();
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize time picker dialog
                 timePicker = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                // initialize hour and minute
                                tHour = hour;
                                tMinute = minute;

                                getIntent().putExtra("tHour", tHour);
                                getIntent().putExtra("tMinute", tMinute);
                                // Store hour and minute in string
                                String time = tHour + ":" + tMinute;
                                // Init 24 hours time format
                                SimpleDateFormat t24Hours= new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = t24Hours.parse(time);
                                    //Set selected time on text view
                                    timeView.setText(t24Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },24,0,true
                );
                // Display previous selected time
                timePicker.updateTime(tHour, tMinute);
                //Show dialog
                timePicker.show();
            }
        });
    }
    /** handle menu **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, Settings_Activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    public void setTime(View view) {
        alarm.onReceive(context, getIntent());
        alarm.setAlarm(context);
    }
    public void unsetTime(View view) {

    }
}