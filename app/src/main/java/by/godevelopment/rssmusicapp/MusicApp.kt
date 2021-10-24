package by.godevelopment.rssmusicapp

import android.app.Application
import by.godevelopment.rssmusicapp.media.PlayList

class MusicApp : Application() {

    val playList = PlayList

    override fun onCreate() {
        super.onCreate()

    }
}
