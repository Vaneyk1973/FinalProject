package com.example.finalproject.service

import android.content.Context
import android.media.MediaPlayer

data class Music(var mPlayer:MediaPlayer?=null) {
    fun stop() {
        if (mPlayer != null) {
            mPlayer!!.release();
            mPlayer = null;
        }
    }

    fun start(c:Context, track:Int) {
        stop();
        mPlayer = MediaPlayer.create(c, track)
        mPlayer!!.setOnCompletionListener { MediaPlayer.OnCompletionListener{
            start(c, track)
        } }
        mPlayer!!.start()
    }
}