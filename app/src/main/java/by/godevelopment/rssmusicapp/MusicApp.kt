package by.godevelopment.rssmusicapp

import android.app.Application
import by.godevelopment.rssmusicapp.media.MusicPlayerHandler

class MusicApp : Application() {
    private var _musicPlayerHandler: MusicPlayerHandler? = null
    val musicPlayerHandler: MusicPlayerHandler
        get() = _musicPlayerHandler!!

    override fun onCreate() {
        super.onCreate()
        _musicPlayerHandler = MusicPlayerHandler(this)
    }
}
