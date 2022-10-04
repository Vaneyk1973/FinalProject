package com.example.finalproject.service

import android.content.Context
import android.media.MediaPlayer
import java.security.KeyStore.TrustedCertificateEntry

class Music() {

    private lateinit var mPlayer: MediaPlayer
    private var p = false

    fun stop() {
        if (p)
            mPlayer.release()
        p = false
    }

    fun start(context: Context, track: Int) {
        stop()
        mPlayer = MediaPlayer.create(context, track)
        mPlayer.setOnCompletionListener { start(context, track) }
        mPlayer.start()
        p = true
    }

}