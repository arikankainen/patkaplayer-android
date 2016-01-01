package com.arik.patkaplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Boolean showBack;

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private ArrayList folderNames = new ArrayList();
    private String sdcardFolder;
    private String curFolder;
    private boolean isFileList = false;
    private int listPosition = 0;
    private int listPositionPixels = 0;
    private boolean timerActive = false;
    private int timerMin = 1;
    private int timerMax = 10;
    private Random rndDelay = new Random();
    private PlayFile play = new PlayFile();
    private String currentFolder;

    private ArrayList<File> allFiles = new ArrayList<>();
    private ArrayList<File> folderFiles = new ArrayList<>();
    private Random rnd = new Random();
    private Menu menu;
    private static final String PREFS_NAME = "prefs";
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    // ****** OVERRIDE ****************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showBack = false;

        try {
            powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PPWakelock");
        }
        catch (Exception ex) {
        }

        String version = BuildConfig.VERSION_NAME;
        TextView ver = (TextView) findViewById(R.id.txtPlaying);
        ver.setText("v" + version);

        if (savedInstanceState != null) {
            showBack = savedInstanceState.getBoolean("showBack");
        }

        try {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            initControls();

            File sdcard = new File(Environment.getExternalStorageDirectory(), "Pätkät");
            sdcardFolder = sdcard.getAbsolutePath();

            File[] allFolders = sdcard.listFiles();
            for (File folder : allFolders) {
                if (folder.isDirectory()) {
                    File[] files = folder.listFiles();
                    for (File file : files) {
                        if (file.isFile()) allFiles.add(file);
                    }
                }
            }

            for (File folder : allFolders) {
                if (folder.isDirectory()) folderNames.add(folder.getName());
            }

            setFolders();
        }
        catch (Exception ex) {

            final Context context = this;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title
            alertDialogBuilder.setTitle("Folder not accessible.");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Folder \"Pätkät\" not found or access is denied.\r\n\r\nMake sure storage permission is granted in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

    }

    @Override
    protected void onStart() {
        initControls();

        super.onStart();
    }

    @Override
    protected void onResume() {
        //readTimerPrefs();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isFileList) {
            setFoldersFade();
        }
        else {
            //play.Stop();
            //super.onBackPressed();
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("showBack", showBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        timerOff();

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
            if (timerActive) timerOff();
            else timerOn();

            return true;
        }

        else if (id == android.R.id.home ){
            setFoldersFade();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean changed = false, up = false, down = false;

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            changed = true;
            up = true;
        }

        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            changed = true;
            down = true;
        }

        if (changed) {
            volumeSeekbar = (SeekBar)findViewById(R.id.seekVolume);

            int progress = volumeSeekbar.getProgress();
            int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            if (up && progress < maxVol) progress++;
            if (down && progress > 0) progress--;

            volumeSeekbar.setProgress(progress);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // ****** METHODS *****************************************************************************

    private void initControls()
    {
        try {
            volumeSeekbar = (SeekBar)findViewById(R.id.seekVolume);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        }

        catch (Exception e) {
            e.printStackTrace();
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

    public void onRandomClicked(View v){

        int i;
        File mp3;

        if (isFileList) {
            i = rnd.nextInt(folderFiles.size());
            mp3 = folderFiles.get(i);
        }
        else {
            i = rnd.nextInt(allFiles.size());
            mp3 = allFiles.get(i);
        }

        playFile(mp3);
    }

    public void onRepeatClicked(View v){
        play.Repeat();
    }

    public void onStopClicked(View v){
        play.Pause();
    }

    private void playFile(File mp3)
    {
        int lastIndex = mp3.getPath().lastIndexOf(File.separator);
        String folderFull = mp3.getPath().substring(0, mp3.getPath().lastIndexOf(File.separator));

        int lastIndex2 = folderFull.lastIndexOf(File.separator);
        String folderFull2 = folderFull.substring(lastIndex2 + 1);

        String name = mp3.getName().replace(".mp3", "");

        TextView tf = (TextView)findViewById(R.id.txtPlayingFolder);
        tf.setText(folderFull2);

        TextView t = (TextView)findViewById(R.id.txtPlaying);
        t.setText(name);

        play.Play(mp3);
    }

    private void setFilesFade(String folder)
    {
        //SystemClock.sleep(200);

        currentFolder = folder;
        ListView list = (ListView) findViewById(R.id.listMp3);
        list.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //mLoadingView.setVisibility(View.GONE);
                        setFiles(currentFolder);

                        ListView list = (ListView) findViewById(R.id.listMp3);
                        list.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .setListener(null);

                    }
                });
    }

    private void setFiles(String folder)
    {
        File subtitle = new File(folder);
        getSupportActionBar().setSubtitle(subtitle.getName());
        //getSupportActionBar().setIcon(null);

        showBackButton();
        savePosition();

        isFileList = true;
        ArrayList fileNames = new ArrayList();
        folderFiles.clear();

        curFolder = folder;
        File f = new File(folder);
        File[] files = f.listFiles();

        for (File file : files) {
            fileNames.add(file.getName().replace(".mp3", ""));
            folderFiles.add(file);
        }

        ArrayAdapter<String> mp3Adapter = new ArrayAdapter<String>(this, R.layout.custom_list_item_multiple_choice, fileNames);
        final ListView mp3List = (ListView) findViewById(R.id.listMp3);
        mp3List.setAdapter(mp3Adapter);

        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();

                String fullname = combine(curFolder, item + ".mp3");
                File mp3 = new File(fullname);
                playFile(mp3);
            }
        });
    }

    private void setFoldersFade()
    {
        ListView list = (ListView) findViewById(R.id.listMp3);
        list.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //mLoadingView.setVisibility(View.GONE);
                        setFolders();

                        ListView list = (ListView) findViewById(R.id.listMp3);
                        list.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .setListener(null);

                    }
                });
    }

    private void setFolders()
    {
        getSupportActionBar().setSubtitle("");
        //getSupportActionBar().setIcon(R.drawable.ppicon32);
        hideBackButton();

        isFileList = false;
        ArrayAdapter<String> mp3Adapter = new ArrayAdapter<String>(this, R.layout.custom_list_item_multiple_choice, folderNames);
        final ListView mp3List = (ListView) findViewById(R.id.listMp3);
        mp3List.setAdapter(mp3Adapter);

        restorePosition();

        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();

                setFilesFade(combine(sdcardFolder, item));
            }
        });
    }

    private void savePosition()
    {
        ListView list = (ListView) findViewById(R.id.listMp3);
        View v = list.getChildAt(0);
        listPosition = list.getFirstVisiblePosition();
        listPositionPixels = (v == null) ? 0 : (v.getTop() - list.getPaddingTop());
    }

    private void restorePosition()
    {
        ListView list = (ListView) findViewById(R.id.listMp3);
        list.setSelectionFromTop(listPosition, listPositionPixels);
    }

    public static String combine (String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    private Runnable timerClip = new Runnable() {
        public void run() {

            if (timerActive) {
                Button btn = (Button) findViewById(R.id.btnRandom);
                btn.performClick();
                int delay = rndDelay.nextInt(timerMax - timerMin) + timerMin;
                handler.postDelayed(this, delay * 1000);
            }
        }
    };

    private void timerOn() {
        //PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        //PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelock");
        wakeLock.acquire();

        MenuItem item = menu.findItem(R.id.action_timer);
        item.getIcon().setAlpha(255);
        readTimerPrefs();
        timerActive = true;
        int delay = rndDelay.nextInt(timerMax - timerMin) + timerMin;
        handler.postDelayed(timerClip, delay * 1000);
    }

    private void timerOff() {
        //PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        //PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelock");
        if (wakeLock.isHeld()) wakeLock.release();

        MenuItem item = menu.findItem(R.id.action_timer);
        item.getIcon().setAlpha(40);
        timerActive = false;
    }

    private void readTimerPrefs() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        Integer timerMinHours = Integer.valueOf(settings.getString("TimerMinHours", "0"));
        Integer timerMinMinutes = Integer.valueOf(settings.getString("TimerMinMinutes", "0"));
        Integer timerMinSeconds = Integer.valueOf(settings.getString("TimerMinSeconds", "30"));
        Integer timerMaxHours = Integer.valueOf(settings.getString("TimerMaxHours", "0"));
        Integer timerMaxMinutes = Integer.valueOf(settings.getString("TimerMaxMinutes", "2"));
        Integer timerMaxSeconds = Integer.valueOf(settings.getString("TimerMaxSeconds", "0"));

        timerMin = (timerMinHours * 60 * 60) + (timerMinMinutes * 60) + timerMinSeconds;
        timerMax = (timerMaxHours * 60 * 60) + (timerMaxMinutes * 60) + timerMaxSeconds;
    }

}
