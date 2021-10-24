package by.godevelopment.rssmusicapp.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.helper.NotificationHelper
import by.godevelopment.rssmusicapp.model.MusicItem
import by.godevelopment.rssmusicapp.model.MusicState

class MusicService : Service() {

    private var musicState = MusicState.NULL
    private var musicMediaPlayer: MediaPlayer? = null

    private val helper by lazy { NotificationHelper(this) }

    private val songs: String = "https://freepd.com/music/Desert Fox.mp3"

    // TODO(): Define MusicBinder() variable
    private val binder by lazy { MusicBinder() }

    // TODO(20): Add onBind()
    override fun onBind(intent: Intent?): IBinder? = binder

    fun initializeMediaPlayer(musicItem: MusicItem) {
        // TODO(21): Initialize Media Player
        Log.i("RssMusicApp", "MusicService: Initialize Media Player")
        musicMediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, Uri.parse(musicItem.trackUri))
            isLooping = false
        }
        musicState = MusicState.STOP
    }

    fun startMusic() {
        Log.i("RssMusicApp", "MusicService: startMusic()")
        musicMediaPlayer = MediaPlayer.create(this, R.raw.test).apply {
            isLooping = true
        }
        musicMediaPlayer?.start()
        musicState = MusicState.PLAY
    }

    fun pauseMusic() {
        Log.i("RssMusicApp", "MusicService: pauseMusic()")
        musicMediaPlayer?.pause()
        musicState = MusicState.PAUSE
    }

    fun stopMusic() {
        Log.i("RssMusicApp", "MusicService: stopMusic()")
        musicMediaPlayer?.run {
            stop()
            release()
        }
        musicState = MusicState.STOP
    }

    fun getCurrentSecondsMedia(): Int {

        return musicMediaPlayer?.currentPosition ?: 0
    }

    fun getMaxSecondsMedia(): Int {
        Log.i("RssMusicApp", "MusicService: getMaxSecondsMedia()")
        return musicMediaPlayer?.duration  ?: 0
    }

    fun setProgressPlayingMedia(progress: Int) {
        Log.i("RssMusicApp", "MusicService: setProgressPlayingMedia()")
        musicMediaPlayer?.seekTo(progress * SECOND)
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO = "?"
        stopService()
    }

    private fun stopService() {
        Log.i("RssMusicApp", "MusicService: stopService()")
        stopForeground(true)
        stopSelf()
    }

    companion object {
        const val SECOND = 1000
    }

    // TODO(21): create binder - MusicBinder
    // MusicBinder is nested inside another class and it can access all methods from it.
    // It can use all of Binder’s methods as well.
    // Inside of it, you create a method for retrieving a service context.
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}
