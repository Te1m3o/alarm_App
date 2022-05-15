package com.example.alarm_app;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyBroadcastReceiver extends BroadcastReceiver {
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";
    private int tHour, tMinute;
    AlarmManager alarmManager;
    Intent intent;
    private LocalBroadcastManager broadcaster;
    PendingIntent pendingIntent;
    Calendar cal_alarm;
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
    public void sendResult(String message) {
        Intent intent = new Intent(COPA_RESULT);
        if(message != null)
            intent.putExtra(COPA_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }
    public void setAlarm(Context context) {
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show();

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
        Intent i = new Intent(context, Snooze_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,12345,i,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
    }
    public void updateTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs",0);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        tHour = calendar.get(calendar.HOUR_OF_DAY) + sharedPreferences.getInt("hour",0);
        tMinute = calendar.get(calendar.MINUTE) + sharedPreferences.getInt("minute",0);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Toast.makeText(context, "Alarm updated successfully", Toast.LENGTH_SHORT).show();
        cal_alarm = Calendar.getInstance();
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
        intent = new Intent(context, Snooze_Activity.class);
        pendingIntent = PendingIntent.getActivity(context,12345,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(),pendingIntent);
        broadcaster = LocalBroadcastManager.getInstance(context);
        sendResult("time updated");
        sendNotification(context);
    }
    public void sendNotification(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"alarmSnooze")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm Manager")
                .setContentText("Alarm klingt nach einer Minute wieder")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
    }
    public void stopAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, Snooze_Activity.class);
        pendingIntent = PendingIntent.getActivity(context,12345,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm stopped successfully", Toast.LENGTH_SHORT).show();
        broadcaster = LocalBroadcastManager.getInstance(context);
        sendResult("alarm stop");

    }
}
