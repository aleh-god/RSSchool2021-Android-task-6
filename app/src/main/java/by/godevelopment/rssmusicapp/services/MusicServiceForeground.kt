package by.godevelopment.rssmusicapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import by.godevelopment.rssmusicapp.helper.NotificationHelper
import by.godevelopment.rssmusicapp.model.MusicItem
import by.godevelopment.rssmusicapp.model.MusicState
import by.godevelopment.rssmusicapp.ui.main.MUSIC_ACTION

const val SERVICE_COMMAND = "Command"
const val SERVICE_MUSIC = "music"
const val BROADCAST_COMMAND = "NotificationText"

class MusicServiceForeground : Service() {

    private val helper by lazy { NotificationHelper(this) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.extras?.run {
            var musicItem = MusicItem("null", "null","null","null",0)
            val musicItemOrNull = getParcelable<MusicItem>(SERVICE_MUSIC)
            if (musicItemOrNull != null) musicItem = musicItemOrNull
            when (getSerializable(SERVICE_COMMAND) as MusicState) {
                MusicState.PLAY -> startMusic(musicItem)
                MusicState.STOP -> endMusicService()
                MusicState.PAUSE -> startMusic(musicItem)
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    private fun startMusic(musicItem: MusicItem) {
        startForeground(NotificationHelper.NOTIFICATION_ID, helper.getNotification(musicItem))
    }

    private fun endMusicService() {
        stopService()
    }

    private fun sendMessageBroadcast() { // update notification
        sendBroadcast(Intent(MUSIC_ACTION).putExtra(BROADCAST_COMMAND, 0))
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }
}
