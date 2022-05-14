package com.example.alarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.timeView);
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
    public void setTime(View view) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Toast.makeText(this, "Time set successfully", Toast.LENGTH_SHORT).show();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY, tHour);
        cal_alarm.set(Calendar.MINUTE, tMinute);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE, 1);
        }
        Intent i = new Intent(MainActivity.this,Snooze.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,12345,i,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);

    }
    public void unsetTime(View view) {

    }
}