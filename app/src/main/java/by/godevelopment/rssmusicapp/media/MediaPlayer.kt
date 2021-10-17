package by.godevelopment.rssmusicapp.media

import android.media.MediaPlayer
import android.net.Uri

class MediaPlayer {

    private val mediaPlayer = MediaPlayer()
    private lateinit var selectedVideoUri: Uri

    companion object {
        const val GET_VIDEO = 123
        const val SECOND = 1000
        const val URL =
            "https://res.cloudinary.com/dit0lwal4/video/upload/v1597756157/samples/elephants.mp4"
    }

    // Create extension properties to get the media player total duration and current duration in seconds
    private val MediaPlayer.seconds: Int // These extension properties help you implement those functionalities.
        get() {
            return this.duration / SECOND
        }

    private val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / SECOND
        }
}
