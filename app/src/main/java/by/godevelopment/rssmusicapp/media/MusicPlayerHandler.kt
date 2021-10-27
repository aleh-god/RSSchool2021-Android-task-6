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
import android.media.AudioAttributes
import com.google.android.material.internal.ContextUtils.getActivity


class MusicPlayerHandler(private val context: Context) {

    private val mediaSource = MusicSource(context)

    private var musicState = MusicState.NULL
    private var musicPlayer: MediaPlayer? = null

    init {
        //        mediaPlayer = MediaPlayer().apply {
//            setDataSource(context, Uri.parse(musicItem.trackUri))
//            isLooping = false
//        }
    }

    private fun setMusicPlayer(musicItem: MusicItem) {
        Log.i("RssMusicApp", "MusicPlayerHandler: setMediaPlayer = ${musicItem.title} = ${mediaSource.playPosition}")
        musicPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(musicItem.trackUri)
            prepare()
            start()
        }
        musicState = MusicState.STOP
    }

    fun startMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: startMusic() = $musicState")
        if (musicState == MusicState.PAUSE) {
            musicPlayer?.start()
        } else {
            setMusicPlayer(getCurrentMusicItem())
            musicPlayer?.start()
        }
        musicState = MusicState.PLAY
    }

    fun pauseMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: pauseMusic()")
        musicPlayer?.pause()
        musicState = MusicState.PAUSE
    }

    fun stopMusic() {
        if (musicState != MusicState.STOP) {
            Log.i("RssMusicApp", "MusicPlayerHandler: stopMusic()")
            musicPlayer?.run {
                stop()
                release()
            }
            musicState = MusicState.STOP
        }
    }

    fun startNextMusic() {
        stopMusic()
        mediaSource.getNextMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startNextMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMusicPlayer(it)
            musicPlayer?.start()
            musicState = MusicState.PLAY
        }
    }

    fun startPrevMusic() {
        stopMusic()
        mediaSource.getPrevMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startPrevMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMusicPlayer(it)
            musicPlayer?.start()
            musicState = MusicState.PLAY
        }
    }

    fun getCurrentSecondsMedia(): Int {
        return musicPlayer?.currentPosition ?: 0
    }

    fun getMaxSecondsMedia(): Int {
        Log.i("RssMusicApp", "MusicService: getMaxSecondsMedia()")
        return musicPlayer?.duration  ?: 0
    }

    fun setProgressPlayingMedia(progress: Int) {
        Log.i("RssMusicApp", "MusicService: setProgressPlayingMedia()")
        musicPlayer?.seekTo(progress * SECOND)
    }

    fun getCurrentMusicItem(): MusicItem = mediaSource.getCurrentMusicItem(mediaSource.playPosition)

    fun getCurrentMusicState(): MusicState = musicState

    fun chekNextMusicItem(): Boolean = mediaSource.chekNextMusicItem()

    fun chekPrevMusicItem(): Boolean = mediaSource.chekPrevMusicItem()

    companion object {
        const val SECOND = 1000
    }
}
