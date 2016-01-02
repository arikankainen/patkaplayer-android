package com.arik.patkaplayer;

import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

public class PlayFile {

    private MediaPlayer mp = new MediaPlayer();

    public Boolean isPlaying()
    {
        return mp.isPlaying();
    }

    public void Play(File mp3)
    {
        mp.reset();
        try {
            mp.setDataSource(mp3.getAbsolutePath());
            mp.prepare();
        }

        catch (IOException e) {
        }

        mp.start();
    }

    public void Repeat(){
        mp.pause();
        mp.seekTo(0);
        mp.start();
    }

    public void Pause() {
        mp.pause();
    }

    public void Stop() {
        mp.reset();
    }
}
