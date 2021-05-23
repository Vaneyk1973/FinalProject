package com.example.finalproject.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Music {
    private MediaPlayer mPlayer;
    private boolean paused = false;

    public Music(){}

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        paused = false;
    }

    public void start(Context c, int track) {
        stop();
        mPlayer = android.media.MediaPlayer.create(c, track);
        mPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                start(c, track);
            }
        });
        paused = false;
        mPlayer.start();
    }
}
