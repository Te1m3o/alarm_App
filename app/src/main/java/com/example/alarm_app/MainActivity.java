package com.example.alarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    int tHour, tMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.timeView);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize time picker dialog
                TimePickerDialog timePicker = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                // initialize hour and minute
                                tHour = hour;
                                tMinute = minute;
                                // Init Calendar
                                // Store hour and minute in string
                                String time = tHour + ":" + tMinute;
                                // Init 24 hours time format
                                SimpleDateFormat t24Hours= new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = t24Hours.parse(time);
                                    // Initialize 12 hours time format
                                    SimpleDateFormat t12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected time on text view
                                    timeView.setText(t12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false
                );
                // Display previous selected time
                timePicker.updateTime(tHour, tMinute);
                //Show dialog
                timePicker.show();
            }
        });
    }
}