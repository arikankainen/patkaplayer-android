package com.arik.patkaplayer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        String timerMinHours = settings.getString("TimerMinHours", "0");
        String timerMinMinutes = settings.getString("TimerMinMinutes", "0");
        String timerMinSeconds = settings.getString("TimerMinSeconds", "30");
        String timerMaxHours = settings.getString("TimerMaxHours", "0");
        String timerMaxMinutes = settings.getString("TimerMaxMinutes", "2");
        String timerMaxSeconds = settings.getString("TimerMaxSeconds", "0");

        NumberPicker numMinHours = (NumberPicker) findViewById(R.id.numMinHours);
        numMinHours.setMinValue(0);
        numMinHours.setMaxValue(23);
        numMinHours.setValue(Integer.valueOf(timerMinHours));
        numMinHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numMinMinutes = (NumberPicker) findViewById(R.id.numMinMinutes);
        numMinMinutes.setMinValue(0);
        numMinMinutes.setMaxValue(59);
        numMinMinutes.setValue(Integer.valueOf(timerMinMinutes));
        numMinMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numMinSeconds = (NumberPicker) findViewById(R.id.numMinSeconds);
        numMinSeconds.setMinValue(0);
        numMinSeconds.setMaxValue(59);
        numMinSeconds.setValue(Integer.valueOf(timerMinSeconds));
        numMinSeconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numMaxHours = (NumberPicker) findViewById(R.id.numMaxHours);
        numMaxHours.setMinValue(0);
        numMaxHours.setMaxValue(23);
        numMaxHours.setValue(Integer.valueOf(timerMaxHours));
        numMaxHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numMaxMinutes = (NumberPicker) findViewById(R.id.numMaxMinutes);
        numMaxMinutes.setMinValue(0);
        numMaxMinutes.setMaxValue(59);
        numMaxMinutes.setValue(Integer.valueOf(timerMaxMinutes));
        numMaxMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numMaxSeconds = (NumberPicker) findViewById(R.id.numMaxSeconds);
        numMaxSeconds.setMinValue(0);
        numMaxSeconds.setMaxValue(59);
        numMaxSeconds.setValue(Integer.valueOf(timerMaxSeconds));
        numMaxSeconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        checkValues();
    }

    private void checkValues() {
        NumberPicker numMinHours = (NumberPicker) findViewById(R.id.numMinHours);
        NumberPicker numMinMinutes = (NumberPicker) findViewById(R.id.numMinMinutes);
        NumberPicker numMinSeconds = (NumberPicker) findViewById(R.id.numMinSeconds);
        NumberPicker numMaxHours = (NumberPicker) findViewById(R.id.numMaxHours);
        NumberPicker numMaxMinutes = (NumberPicker) findViewById(R.id.numMaxMinutes);
        NumberPicker numMaxSeconds = (NumberPicker) findViewById(R.id.numMaxSeconds);

        Integer timerMinHours = numMinHours.getValue();
        Integer timerMinMinutes = numMinMinutes.getValue();
        Integer timerMinSeconds = numMinSeconds.getValue();
        Integer timerMaxHours = numMaxHours.getValue();
        Integer timerMaxMinutes = numMaxMinutes.getValue();
        Integer timerMaxSeconds = numMaxSeconds.getValue();

        Integer timerMin = (timerMinHours * 60 * 60) + (timerMinMinutes * 60) + timerMinSeconds;
        Integer timerMax = (timerMaxHours * 60 * 60) + (timerMaxMinutes * 60) + timerMaxSeconds;

        if (timerMin < 1) {
            numMinSeconds.setValue(1);
            timerMinSeconds = 1;
        }

        if (timerMin > 86398) {
            numMinSeconds.setValue(58);
            timerMinSeconds = 58;
        }

        if (timerMax <= timerMin) {

            if (timerMinSeconds < 59) {
                numMaxHours.setValue(timerMinHours);
                numMaxMinutes.setValue(timerMinMinutes);
                numMaxSeconds.setValue(timerMinSeconds + 1);
            }

            else if (timerMinMinutes < 59) {
                numMaxHours.setValue(timerMinHours);
                numMaxMinutes.setValue(timerMinMinutes + 1);
                numMaxSeconds.setValue(0);
            }

            else {
                numMaxHours.setValue(timerMinHours + 1);
                numMaxMinutes.setValue(0);
                numMaxSeconds.setValue(0);
            }
        }

    }

    @Override
    protected void onStop(){
        super.onStop();

        NumberPicker numMinHours = (NumberPicker) findViewById(R.id.numMinHours);
        NumberPicker numMinMinutes = (NumberPicker) findViewById(R.id.numMinMinutes);
        NumberPicker numMinSeconds = (NumberPicker) findViewById(R.id.numMinSeconds);
        NumberPicker numMaxHours = (NumberPicker) findViewById(R.id.numMaxHours);
        NumberPicker numMaxMinutes = (NumberPicker) findViewById(R.id.numMaxMinutes);
        NumberPicker numMaxSeconds = (NumberPicker) findViewById(R.id.numMaxSeconds);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("TimerMinHours", String.valueOf(numMinHours.getValue()));
        editor.putString("TimerMinMinutes", String.valueOf(numMinMinutes.getValue()));
        editor.putString("TimerMinSeconds", String.valueOf(numMinSeconds.getValue()));
        editor.putString("TimerMaxHours", String.valueOf(numMaxHours.getValue()));
        editor.putString("TimerMaxMinutes", String.valueOf(numMaxMinutes.getValue()));
        editor.putString("TimerMaxSeconds", String.valueOf(numMaxSeconds.getValue()));

        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
