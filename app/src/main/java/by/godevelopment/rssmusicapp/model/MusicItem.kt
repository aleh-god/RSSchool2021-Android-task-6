package by.godevelopment.rssmusicapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicItem(
    val title: String,
    val artist: String,
    val bitmapUri: String,
    val trackUri: String,
    val duration: Int
) {
    override fun toString(): String {
        return "MusicItem: $title"
    }
}
