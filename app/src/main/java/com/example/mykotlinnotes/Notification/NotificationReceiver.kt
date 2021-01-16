package com.example.mykotlinnotes.Notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mykotlinnotes.R
import com.example.mykotlinnotes.Screens.MainActivity
import java.util.*

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //creating unique id for each specific notification
        val ID : Int = System.currentTimeMillis().toInt()

        val manager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)

        val title = intent?.getStringExtra("Title")
        val description = intent?.getStringExtra("Description")
        val builder: NotificationCompat.Builder? = context?.let {
            NotificationCompat.Builder(it, "taskNotify")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        }

        manager.notify(ID, builder?.build())
    }
}