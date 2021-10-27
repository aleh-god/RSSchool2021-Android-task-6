package by.godevelopment.rssmusicapp.model

import com.squareup.moshi.JsonClass
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MusicItem(
    val title: String,
    val artist: String,
    val bitmapUri: String,
    val trackUri: String,
    val duration: Int
) : Parcelable
