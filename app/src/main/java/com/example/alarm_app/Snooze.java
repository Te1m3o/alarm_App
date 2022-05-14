package com.example.alarm_app;

import android.app.PendingIntent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Snooze extends AppCompatActivity {
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        player = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        startAlarm();
    }
    private void startAlarm() {
        player.setLooping(true);
        player.start();
    }
    public void stopAlarm(View view) {
        player.stop();
        Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show();
    }
}
