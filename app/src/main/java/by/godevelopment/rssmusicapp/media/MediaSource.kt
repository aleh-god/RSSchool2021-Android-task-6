package by.godevelopment.rssmusicapp.media

import android.content.Context
import android.util.Log
import by.godevelopment.rssmusicapp.model.MusicItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class MusicSource(context: Context) {

    private val nullMusicItem = MusicItem("Null","Null","R.drawable.ic_music_null.jpg","android.resource://by.godevelopment.rssmusicapp/raw/test",1)

    private var playList: List<MusicItem> = listOf(
        MusicItem("Ice and Snow","Rafael Krux","https://www.pelicanwater.com/blog/wp-content/uploads/2019/02/cropped-Snow_Blog_Jan_2019-01.jpg","https://freepd.com/music/Ice and Snow.mp3",141000),
        MusicItem("Desert Fox", "Rafael Krux","https://i.natgeofe.com/k/1db1b816-aa92-434e-994f-d3298c9f58f8/fennec-fox-hole.jpg","https://freepd.com/music/Desert Fox.mp3",133000),
        MusicItem("Coy Koi", "Frank Nora","https://m.media-amazon.com/images/I/81y0ZQKOi0L._AC_SL1429_.jpg","https://freepd.com/music/Coy Koi.mp3",48000)
    )

    var playPosition: Int = 0
        private set

    init{
        Log.i("RssMusicApp", "MusicSource INIT")

        val moshi = Moshi.Builder().build()
        val arrayType = Types.newParameterizedType(List::class.java, MusicItem::class.java)
        val adapter: JsonAdapter<List<MusicItem>> = moshi.adapter(arrayType)
        val file = "playlist.json"
        val jsonReader = context.assets.open(file).bufferedReader().use { it.readText() }
        adapter.fromJson(jsonReader)?.let { playList = it }
    }

    fun getNextMusicItem(): MusicItem? {
        return if (chekNextMusicItem()) {
            Log.i("RssMusicApp", "MusicSource getNextMusicItem() = $playPosition")
            playList[++playPosition]
        } else null
    }

    fun getPrevMusicItem(): MusicItem? {
        return if (chekPrevMusicItem()) {
            Log.i("RssMusicApp", "MusicSource getPrevMusicItem() = $playPosition")
            playList[--playPosition]
        } else null
    }

    fun getCurrentMusicItem(position: Int): MusicItem {
        return if (position in 0..playList.lastIndex) {
            Log.i("RssMusicApp", "MusicSource getCurrentMusicItem() = $position")
            playList[position]
        } else return nullMusicItem
    }

    fun chekNextMusicItem(): Boolean = (playPosition < playList.lastIndex)

    fun chekPrevMusicItem(): Boolean = (playPosition > 0)
}
