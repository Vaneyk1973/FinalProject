package com.example.finalproject.service

import android.content.Context
import android.media.MediaPlayer

class Music(var mPlayer:MediaPlayer?) {
    fun stop() {
        mPlayer?.release()
        mPlayer=null
    }

    fun start(context:Context, track:Int) {
        stop();
        mPlayer = MediaPlayer.create(context, track)
        mPlayer?.setOnCompletionListener {start(context, track)}
        mPlayer?.start()
    }
}