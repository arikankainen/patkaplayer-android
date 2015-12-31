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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String timerMinDelay = settings.getString("TimerMinDelay", "30");
        String timerMaxDelay = settings.getString("TimerMaxDelay", "120");

        NumberPicker numberPickerMin = (NumberPicker) findViewById(R.id.numMin);
        numberPickerMin.setMinValue(5);
        numberPickerMin.setMaxValue(3600);
        numberPickerMin.setWrapSelectorWheel(false);
        numberPickerMin.setValue(Integer.valueOf(timerMinDelay));

        numberPickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        NumberPicker numberPickerMax = (NumberPicker) findViewById(R.id.numMax);
        numberPickerMax.setMinValue(5);
        numberPickerMax.setMaxValue(3600);
        numberPickerMax.setWrapSelectorWheel(false);
        numberPickerMax.setValue(Integer.valueOf(timerMaxDelay));
        numberPickerMax.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                checkValues();
            }
        });

        checkValues();

    }

    private void checkValues() {
        NumberPicker numberPickerMin = (NumberPicker) findViewById(R.id.numMin);
        NumberPicker numberPickerMax = (NumberPicker) findViewById(R.id.numMax);

        numberPickerMax.setMinValue(numberPickerMin.getValue() + 1);
        numberPickerMin.setMaxValue(numberPickerMax.getValue() - 1);
    }

    @Override
    protected void onStop(){
        super.onStop();

        NumberPicker numberPickerMin = (NumberPicker) findViewById(R.id.numMin);
        NumberPicker numberPickerMax = (NumberPicker) findViewById(R.id.numMax);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("TimerMinDelay", String.valueOf(numberPickerMin.getValue()));
        editor.putString("TimerMaxDelay", String.valueOf(numberPickerMax.getValue()));

        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
