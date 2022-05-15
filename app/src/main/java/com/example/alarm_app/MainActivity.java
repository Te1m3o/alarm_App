package com.example.alarm_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    TimePickerDialog timePicker;
    public int tHour, tMinute;
    private MyBroadcastReceiver alarm;
    Context context;
    BroadcastReceiver receiver;
    String time;
    boolean notificationSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        timeView = findViewById(R.id.timeView);
        Bundle extras = getIntent().getExtras();
        /** Check if the alarm is already snoozed*/
        if (extras != null) {
           notificationSent = extras.getBoolean("notification");
        }
        /** If it is already snoozed show the next ring time on the time viewer **/
        if (notificationSent == true){
            Log.e("extra", "extra is running");
            boolean notificationSent = extras.getBoolean("notification");
            if (notificationSent==true){
                initTimePicker();
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs",0);
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
                tHour = calendar.get(calendar.HOUR_OF_DAY) + sharedPreferences.getInt("hour",0);
                tMinute = calendar.get(calendar.MINUTE) + sharedPreferences.getInt("minute",0);
                time = tHour + ":" + tMinute;
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
                timePicker.updateTime(tHour,tMinute);
            }
        }
        /** Create the broadcastReceiver to handle the actions, coming from the MyBroadcastReceiver class **/
        alarm = new MyBroadcastReceiver();
        createNotificationChannel();
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(MyBroadcastReceiver.COPA_MESSAGE);
                if (s=="time updated"){
                    MainActivity.this.finish();
                }
                if (s=="alarm stop"){
                    initTimePicker();
                    timeView.setText("Select Time");
                }
            }
        };
            }
    @Override
    /** Register the BroadcastReceiver **/
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(MyBroadcastReceiver.COPA_RESULT)
        );
    }
    /** select time on the timepicker **/
    public void selectTime(View view){
        initTimePicker();
        // Display previous selected time
        timePicker.updateTime(tHour, tMinute);
        //Show dialog
        timePicker.show();
    }
    /** initialize timepicker **/
    public void initTimePicker() {
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
                        time = tHour + ":" + tMinute;
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
    /** Set alarm **/
    public void setTime(View view) {
        alarm.onReceive(context, getIntent());
        alarm.setAlarm(context);
    }
    /** Stop alarm **/
    public void stopAlarm(View view) {
        alarm.stopAlarm(context);
    }
    /** Create notification channel to handle the notification, coming from the MyBroadcastReceiver class*/
    private void createNotificationChannel() {
    CharSequence name = "alarmChannel";
    String description = "Channel for Alarm Manager";
    int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("alarmSnooze", name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}