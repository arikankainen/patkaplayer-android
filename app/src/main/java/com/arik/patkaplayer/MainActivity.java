package com.arik.patkaplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String date;
    private Boolean showBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = DateFormat.getDateTimeInstance().format(new Date());
        showBack = false;

        if (savedInstanceState != null) {
            date = savedInstanceState.getString("date");
            showBack = savedInstanceState.getBoolean("showBack");
        }
    }

    private void hideBackButton() {
        showBack = false;
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
    }

    private void showBackButton() {
        showBack = true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    public void onClicked(View v){
        showBackButton();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("date", date);
        savedInstanceState.putBoolean("showBack", showBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        else if (id == R.id.action_timer) {
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);

            return true;
        }

        else if (id == android.R.id.home ){
            hideBackButton();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
