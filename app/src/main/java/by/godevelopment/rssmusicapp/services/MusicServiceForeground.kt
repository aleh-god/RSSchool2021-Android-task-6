package by.godevelopment.rssmusicapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import by.godevelopment.rssmusicapp.helper.NotificationHelper
import by.godevelopment.rssmusicapp.model.MusicState
import by.godevelopment.rssmusicapp.ui.main.MUSIC_ACTION

const val SERVICE_COMMAND = "Command"
const val NOTIFICATION_TEXT = "NotificationText"

class MusicServiceForeground : Service() {

    private val helper by lazy { NotificationHelper(this) }

    override fun onBind(intent: Intent?): IBinder? = null

    // TODO(4): Implementing the Service Methods onStartCommand()
    // Here, you go through the Intent extras to get the value that the key SERVICE_COMMAND saves.
    // This value indicates which action the service should execute.
    // If the system kills the service because the memory runs out, START_NOT_STICKY tells the system
    // not to recreate the service with an undefined Intent.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.extras?.run {
            when (getSerializable(SERVICE_COMMAND) as MusicState) {
                MusicState.PLAY -> startMusic()
                MusicState.STOP -> endMusicService()
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    // TODO(13): Call startForeground() to publish notification
    // To start the service in the foreground, use startForeground().
    // This method needs two parameters: the unique positive integer ID of the notification and Notificaton.
    private fun startMusic(elapsedTime: Int? = null) {
        startForeground(NotificationHelper.NOTIFICATION_ID, helper.getNotification())
        broadcastUpdate()
    }

    private fun endMusicService() {
        broadcastUpdate()
        stopService()
    }

    // TODO(17): Send broadcast and call updateNotification
    // Here, you send a broadcast with the elapsed time to MainActivity.
    // With it, MainActivity can update the time in the TextView below the card’s view.
    // This helper method updates the status bar notification you posted above.
    // TODO(18): Call updateNotification if timer is paused
    // At this point, you’ve implemented everything the user sees while playing the game.
    // But what happens if the user kills the app while the timer is running?
    private fun broadcastUpdate() { // update notification
        sendBroadcast(Intent(MUSIC_ACTION).putExtra(NOTIFICATION_TEXT, 0))
        helper.updateNotification("test")
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }
}
