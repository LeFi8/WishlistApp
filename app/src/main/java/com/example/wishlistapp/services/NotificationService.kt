package com.example.wishlistapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.wishlistapp.Notification

private const val NOTIFICATION_ID = 1
class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, Notification.createNotification(this))
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder ?= null
}