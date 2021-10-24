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

private const val CHANNEL_ID = "MusicAppChannel"
private const val CHANNEL_NAME = "MusicAppChannel"
private const val CHANNEL_DESCRIPTION = "MusicAppChannelDescription"

class NotificationHelper(private val context: Context) {

    // TODO(9): You’re using NotificationCompat.Builder to create a status bar notification builder.
    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setSound(null)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)   // This kind of notification should be set as auto-cancelable, which means that when the user clicks it, it’s automatically dismissed.
    }

    // TODO(7): Add notificationManager
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // NOTIFICATION_SERVICE system service and casting it as NotificationManager.
    }

    // TODO(12): Add contentIntent. define what happens when a user clicks on the notification.
    private val contentIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    // TODO(10): Define getNotification(). Building a Notification
    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    // TODO(11): Define updateNotification()
    fun updateNotification(notificationText: String? = null) {
        // You update the text in the notification published in the status bar via the notificationBuilder
        notificationText?.let { notificationBuilder.setContentText(it) }
        // Then notify the Notification Manager about which notification to update. To do that, you use a unique NOTIFICATION_ID.
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


    // TODO(8): Define createChannel()
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
