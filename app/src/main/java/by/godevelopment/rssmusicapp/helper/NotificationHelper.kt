package by.godevelopment.rssmusicapp.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import by.godevelopment.rssmusicapp.MainActivity
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.model.MusicItem

private const val CHANNEL_ID = "MusicAppChannel"
private const val CHANNEL_NAME = "MusicAppChannel"
private const val CHANNEL_DESCRIPTION = "MusicAppChannelDescription"

class NotificationHelper(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

//    var notification = NotificationCompat.Builder(context, CHANNEL_ID)
//        // Show controls on lock screen even when user hides sensitive content.
//        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//        .setSmallIcon(R.drawable.ic_stat_player)
//        // Add media control buttons that invoke intents in your media service
//        .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent) // #0
//        .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent) // #1
//        .addAction(R.drawable.ic_next, "Next", nextPendingIntent) // #2
//        // Apply the media style template
//        .setStyle(
//            Notification.MediaStyle())
//                .setShowActionsInCompactView(1 /* #1: pause button \*/))
//        .setContentTitle("Wonderful music")
//        .setContentText("My Awesome Band")
//        .setLargeIcon(albumArtBitmap)

    // create a status bar notification builder.
    private fun getNotificationBuilder(musicItem: MusicItem): NotificationCompat.Builder {
        val notificationBuilder: NotificationCompat.Builder by lazy {
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Title: ${musicItem.title}")
                .setContentText("Artist: ${musicItem.artist}")
                .setSound(null)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_music)
                // .setLargeIcon(musicItem.bitmapUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        }
        return notificationBuilder
    }

    // define what happens when a user clicks on the notification.
    private val contentIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    // Define getNotification(). Building a Notification
    fun getNotification(musicItem: MusicItem): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return getNotificationBuilder(musicItem).build()
    }

    // Define createChannel()
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    companion object {
        const val NOTIFICATION_ID = 7
    }
}
