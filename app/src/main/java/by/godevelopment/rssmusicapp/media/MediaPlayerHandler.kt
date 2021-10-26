package by.godevelopment.rssmusicapp.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.model.MusicItem
import by.godevelopment.rssmusicapp.model.MusicState
import android.app.Activity
import com.google.android.material.internal.ContextUtils.getActivity


class MusicPlayerHandler(private val context: Context) {

    private val mediaSource = MusicSource(context)

    private var musicState = MusicState.NULL
    private var mediaPlayer: MediaPlayer? = null

    private fun setMediaPlayer(musicItem: MusicItem) {
        Log.i("RssMusicApp", "MusicPlayerHandler: setMediaPlayer = ${musicItem.title} = ${mediaSource.playPosition}")
        musicState = MusicState.STOP
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.parse(musicItem.trackUri))
            isLooping = false
        }
    }

    fun startMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: startMusic() = $musicState")
        if (musicState == MusicState.PAUSE) {
            mediaPlayer?.start()
        } else {
            setMediaPlayer(getCurrentMusicItem())
            mediaPlayer?.start()
        }
        musicState = MusicState.PLAY
    }

    fun pauseMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: pauseMusic()")
        mediaPlayer?.pause()
        musicState = MusicState.PAUSE
    }

    fun stopMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: stopMusic()")
        mediaPlayer?.run {
            stop()
            release()
        }
        musicState = MusicState.STOP
    }

    fun startNextMusic() {
        mediaSource.getNextMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startNextMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMediaPlayer(it)
            mediaPlayer?.start()
            musicState = MusicState.PLAY
        }
    }

    fun startPrevMusic() {
        mediaSource.getPrevMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startPrevMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMediaPlayer(it)
            mediaPlayer?.start()
            musicState = MusicState.PLAY
        }
    }

    fun getCurrentSecondsMedia(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getMaxSecondsMedia(): Int {
        Log.i("RssMusicApp", "MusicService: getMaxSecondsMedia()")
        return mediaPlayer?.duration  ?: 0
    }

    fun setProgressPlayingMedia(progress: Int) {
        Log.i("RssMusicApp", "MusicService: setProgressPlayingMedia()")
        mediaPlayer?.seekTo(progress * SECOND)
    }

    fun getCurrentMusicItem(): MusicItem = mediaSource.getCurrentMusicItem(mediaSource.playPosition)

    fun getCurrentMusicState(): MusicState = musicState

    fun chekNextMusicItem(): Boolean = mediaSource.chekNextMusicItem()

    fun chekPrevMusicItem(): Boolean = mediaSource.chekPrevMusicItem()

    companion object {
        const val SECOND = 1000
    }
}
