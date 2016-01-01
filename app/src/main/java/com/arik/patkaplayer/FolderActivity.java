package com.arik.patkaplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FolderActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "prefs";
    private File currentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String setFolder = settings.getString("Folder", null);

        //Toast.makeText(this, "ExternalStorage: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();

        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        if (setFolder != null) {
            File folder = new File(setFolder);
            if (folder.exists()) setListView(folder);
            else setListView(sdcard);
        }

        else {
            setListView(sdcard);
        }
    }

    private void setListView(File folder) {
        currentFolder = folder;
        getSupportActionBar().setSubtitle(folder.getAbsolutePath());

        File[] allFolders = folder.listFiles();
        Arrays.sort(allFolders);

        ArrayList folderNames = new ArrayList();

        for (File folderTemp : allFolders) {
            folderNames.add(folderTemp.getName());
        }

        ArrayAdapter<String> folderAdapter = new ArrayAdapter<String>(this, R.layout.custom_list_item_multiple_choice, folderNames);
        ListView folderList = (ListView) findViewById(R.id.listFolders);
        folderList.setAdapter(folderAdapter);

        folderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();
                File folder = new File(currentFolder, item);
                if (folder.isDirectory()) setListView(folder);
            }
        });
    }

    private File folderDown() {
        File parent;
        String sParent = currentFolder.getParent();
        if (sParent != null) parent = new File(currentFolder.getParent());
        else parent = currentFolder;

        File[] allFolders = parent.listFiles();
        if (allFolders == null || allFolders.length == 0) return currentFolder;

        return parent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setListView(folderDown());
        return true;
    }

    @Override
    public void onBackPressed() {
        setListView(folderDown());
    }

    public void onCancelClicked(View v) {
        this.finish();
    }

    public void onOkClicked(View v) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("Folder", currentFolder.getAbsolutePath());
        editor.commit();

        this.finish();
    }


}
