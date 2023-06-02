package com.example.wishlistapp.mapServices

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.example.wishlistapp.services.NotificationService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices


private const val REQUEST_CODE = 2
object Geofencing {

    fun createGeofence(context: Context, latitude: Double, longitude: Double, id: String) {
        val geofence = Geofence.Builder()
            .setCircularRegion(latitude, longitude, 200f)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setRequestId(id)
            .build()

        val request = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
            .build()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).let {
                context.startActivity(it)
            }
        } else {
            LocationServices.getGeofencingClient(context)
                .addGeofences(request, makePendingIntent(context)).run {
                    addOnFailureListener {
                        println(it)
                    }
                    addOnSuccessListener { println("Added geofencing listener") }
                }
        }
    }

    private fun makePendingIntent(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context, REQUEST_CODE,
            Intent(context, NotificationService::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}