package com.example.alarm_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings_Activity extends AppCompatActivity {
    private EditText hourTB;
    private EditText minuteTB;
    private Button saveBTN;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        hourTB = findViewById(R.id.hourTB);
        minuteTB = findViewById(R.id.minuteTB);
        saveBTN = findViewById(R.id.save);
    }
    public void saveData(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HOUR, Integer.parseInt(hourTB.getText().toString()));
        editor.putInt(MINUTE, Integer.parseInt(minuteTB.getText().toString()));
        editor.commit();
        Toast.makeText(this, "Die snooze Zeit geändert", Toast.LENGTH_SHORT).show();
        finish();
    }
}
