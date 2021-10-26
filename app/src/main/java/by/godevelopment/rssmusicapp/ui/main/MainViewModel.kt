package by.godevelopment.rssmusicapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.godevelopment.rssmusicapp.media.MusicPlayerHandler
import by.godevelopment.rssmusicapp.model.MusicItem
import by.godevelopment.rssmusicapp.model.MusicState

class MainViewModel(private val musicPlayerHandler: MusicPlayerHandler) : ViewModel() {

    fun startMusic() {
        Log.i("RssMusicApp", "MainViewModel: startMusic()")
        musicPlayerHandler.startMusic()
    }

    fun pauseMusic() {
        Log.i("RssMusicApp", "MainViewModel: pauseMusic()")
        musicPlayerHandler.pauseMusic()
    }

    fun stopMusic() {
        Log.i("RssMusicApp", "MainViewModel: stopMusic()")
        musicPlayerHandler.stopMusic()
    }

    fun startNextMusic() {
        Log.i("RssMusicApp", "MainViewModel: startNextMusic()")
        musicPlayerHandler.startNextMusic()
    }

    fun startPrevMusic() {
        Log.i("RssMusicApp", "MainViewModel: startPrevMusic()")
        musicPlayerHandler.startPrevMusic()
    }

    fun getCurrentMusicItem(): MusicItem = musicPlayerHandler.getCurrentMusicItem()

    fun getCurrentMusicState(): MusicState = musicPlayerHandler.getCurrentMusicState()

    fun chekNextMusicItem(): Boolean = musicPlayerHandler.chekNextMusicItem()

    fun chekPrevMusicItem(): Boolean = musicPlayerHandler.chekPrevMusicItem()
}

class MainViewModelFactory(private val musicPlayerHandler: MusicPlayerHandler) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(musicPlayerHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
