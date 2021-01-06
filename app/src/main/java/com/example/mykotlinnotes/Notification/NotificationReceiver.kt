package com.example.mykotlinnotes.Notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mykotlinnotes.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder: NotificationCompat.Builder? = context?.let {
            NotificationCompat.Builder(it, "taskNotify")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Title")
                .setContentText("Description")
        }
        val manager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder?.build())
    }
}