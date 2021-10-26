package by.godevelopment.rssmusicapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MediaServiceForeground : Service(), CoroutineScope {

    override fun onBind(p0: Intent?): IBinder? = null

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
}
