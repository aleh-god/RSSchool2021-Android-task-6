package by.godevelopment.rssmusicapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MusicState : Parcelable {
    PLAY,
    PAUSE,
    STOP,
    NULL
}
