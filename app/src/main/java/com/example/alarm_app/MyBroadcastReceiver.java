package com.example.alarm_app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private int tHour, tMinute;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(extras.containsKey("tHour")){
                Log.e("number1", "got it");
                tHour = extras.getInt("tHour");
            }
            if (extras.containsKey("tMinute")){
                Log.e("number2", "got it");
                tMinute = extras.getInt("tMinute");
            }
        }
    }
    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Toast.makeText(context, "Time set successfully", Toast.LENGTH_SHORT).show();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);
        Log.e("number1", Integer.toString(tHour));
        Log.e("number2", Integer.toString(tMinute));
        cal_alarm.set(Calendar.HOUR_OF_DAY, tHour);
        cal_alarm.set(Calendar.MINUTE, tMinute);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE, 1);
        }
        Intent i = new Intent(context,Snooze.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,12345,i,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
    }
}
