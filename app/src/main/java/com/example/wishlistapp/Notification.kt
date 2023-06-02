package com.example.wishlistapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "ID_CHANNEL_DEFAULT"
object Notification {

    fun createChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, "Wishlist", NotificationManager.IMPORTANCE_DEFAULT)
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun createNotification(context: Context) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification))
        .build()
}