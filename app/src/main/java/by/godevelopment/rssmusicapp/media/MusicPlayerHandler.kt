package by.godevelopment.rssmusicapp.media

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContextCompat
import by.godevelopment.rssmusicapp.model.MusicItem
import by.godevelopment.rssmusicapp.model.MusicState
import by.godevelopment.rssmusicapp.services.MusicServiceForeground
import by.godevelopment.rssmusicapp.services.SERVICE_COMMAND
import by.godevelopment.rssmusicapp.services.SERVICE_MUSIC


class MusicPlayerHandler(private val context: Context) {

    private val mediaSource = MusicSource(context)

    private var musicState = MusicState.NULL
    private var musicPlayer: MediaPlayer? = null

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
        if (musicState != MusicState.PAUSE) setMusicPlayer(getCurrentMusicItem())
        musicPlayer?.start()
        musicState = MusicState.PLAY
        sendCommandToForegroundService(getCurrentMusicItem())
    }

    fun pauseMusic() {
        Log.i("RssMusicApp", "MusicPlayerHandler: pauseMusic()")
        musicPlayer?.pause()
        musicState = MusicState.PAUSE
        sendCommandToForegroundService(getCurrentMusicItem())
    }

    fun stopMusic() {
        if (musicState != MusicState.STOP) {
            Log.i("RssMusicApp", "MusicPlayerHandler: stopMusic()")
            musicPlayer?.run {
                stop()
                release()
            }
            musicState = MusicState.STOP
            sendCommandToForegroundService(getCurrentMusicItem())
        }
    }

    fun startNextMusic() {
        stopMusic()
        mediaSource.getNextMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startNextMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMusicPlayer(it)
            musicPlayer?.start()
            musicState = MusicState.PLAY
            sendCommandToForegroundService(it)
        }
    }

    fun startPrevMusic() {
        stopMusic()
        mediaSource.getPrevMusicItem()?.let {
            Log.i("RssMusicApp", "MusicPlayerHandler: startPrevMusic() = ${it.title} = ${mediaSource.playPosition}")
            setMusicPlayer(it)
            musicPlayer?.start()
            musicState = MusicState.PLAY
            sendCommandToForegroundService(it)
        }
    }

    fun getCurrentMusicItem(): MusicItem = mediaSource.getCurrentMusicItem(mediaSource.playPosition)

    fun getCurrentMusicState(): MusicState = musicState

    fun chekNextMusicItem(): Boolean = mediaSource.chekNextMusicItem()

    fun chekPrevMusicItem(): Boolean = mediaSource.chekPrevMusicItem()

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

    companion object {
        const val SECOND = 1000
    }

    // Call starting a foreground service
    private fun sendCommandToForegroundService(musicItem: MusicItem) {
        ContextCompat.startForegroundService(context, getServiceIntent(musicItem))
    }

    private fun getServiceIntent(musicItem: MusicItem) =
        Intent(context, MusicServiceForeground::class.java).apply {
            putExtra(SERVICE_MUSIC, musicItem)
            putExtra(SERVICE_COMMAND, musicState as Parcelable)
        }

    //    override fun onPrepared(p0: MediaPlayer?) {
    //        binding.progressBar.visibility = View.GONE
    //    }
}
