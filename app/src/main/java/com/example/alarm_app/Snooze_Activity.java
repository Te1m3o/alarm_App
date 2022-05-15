package com.example.alarm_app;

import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Snooze_Activity extends AppCompatActivity {
    private MyBroadcastReceiver alarm;
    MediaPlayer player;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        player = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        alarm = new MyBroadcastReceiver();
        context = this.getApplicationContext();
        startAlarm();
    }
    private void startAlarm() {
        player.setLooping(true);
        player.start();
    }
    public void stopAlarm(View view) {
        player.stop();
        Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show();
        alarm.updateTime(context);
        finish();
    }
}
