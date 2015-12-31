package com.arik.patkaplayer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String audioFolder = settings.getString("AudioFolder", "");
        String timerMinDelay = settings.getString("TimerMinDelay", "30");
        String timerMaxDelay = settings.getString("TimerMaxDelay", "120");

        TextView textAudioFolder = (TextView) findViewById(R.id.audioFolder);
        textAudioFolder.setText(audioFolder);

        TextView textTimerMinDelay = (TextView) findViewById(R.id.timerMinDelay);
        textTimerMinDelay.setText(timerMinDelay);

        TextView textTimerMaxDelay = (TextView) findViewById(R.id.timerMaxDelay);
        textTimerMaxDelay.setText(timerMaxDelay);

    }

    @Override
    protected void onStop(){
        super.onStop();

        TextView textAudioFolder = (TextView) findViewById(R.id.audioFolder);
        TextView textTimerMinDelay = (TextView) findViewById(R.id.timerMinDelay);
        TextView textTimerMaxDelay = (TextView) findViewById(R.id.timerMaxDelay);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("AudioFolder", String.valueOf(textAudioFolder.getText()));
        editor.putString("TimerMinDelay", String.valueOf(textTimerMinDelay.getText()));
        editor.putString("TimerMaxDelay", String.valueOf(textTimerMaxDelay.getText()));

        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
